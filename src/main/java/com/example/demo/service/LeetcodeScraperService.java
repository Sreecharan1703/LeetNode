package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuestionRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class LeetcodeScraperService {
    private final QuestionRepo questionRepo;
    LeetcodeScraperService(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";

    public Map<String, Object> getLeetCodeData(int frontendId){
        String titleSlug = getTitleSlugForId(frontendId);
        if (titleSlug == null) {
            throw new ResourceNotFoundException("Dude, LeetCode doesn't have a question with id: " + frontendId);
        }
        return fetchGraphQLData(titleSlug);
    }

    private static final String PROBLEM_LIST_QUERY = """
        query problemsetQuestionList($categorySlug: String, $limit: Int, $skip: Int, $filters: QuestionListFilterInput) {
          problemsetQuestionList: questionList(
            categorySlug: $categorySlug
            limit: $limit
            skip: $skip
            filters: $filters
          ) {
            total: totalNum
            questions: data {
              questionFrontendId
              titleSlug
            }
          }
        }
        """;

    private String getTitleSlugForId(int frontendId) {
        Optional<String> titleslug = questionRepo.getTitleSlugById(frontendId);
        if(titleslug.isPresent()){
            return titleslug.get();
        }

        String target = String.valueOf(frontendId);
        final int LIMIT = 20;
        int skip = frontendId - 1;

        for (int hop = 0; hop < 5; hop++) {
            JsonNode data = fetchProblemPage(skip, LIMIT);
            JsonNode page = data.path("questions");
            int total = data.path("total").asInt();

            if (page.size() == 0) {
                // skip is past the end of the list -> look at the last page instead
                int lastPageSkip = Math.max(0, total - LIMIT);
                if (total <= 0 || skip == lastPageSkip) return null;
                skip = lastPageSkip;
                continue;
            }

            for (JsonNode q : page) {
                if (q.path("questionFrontendId").asText().equals(target)) {
                    return q.path("titleSlug").asText();
                }
            }

            int first = parseId(page.get(0));
            int last = parseId(page.get(page.size() - 1));
            if (first < 0 || last < 0) return null;

            if (frontendId < first) {
                if (skip == 0) return null;                       // smaller than the smallest id
                skip = Math.max(0, skip - (first - frontendId));
            } else if (frontendId > last) {
                if (skip + page.size() >= total) return null;     // bigger than the largest id
                skip = skip + (page.size() - 1) + (frontendId - last);
            } else {
                return null;                                       // inside the range but missing
            }
        }
        return null;
    }

    private static JsonNode fetchProblemPage(int skip, int limit) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("categorySlug", "");
            variables.put("skip", skip);
            variables.put("limit", limit);
            variables.put("filters", new HashMap<>());

            Map<String, Object> payload = new HashMap<>();
            payload.put("operationName", "problemsetQuestionList");
            payload.put("query", PROBLEM_LIST_QUERY);
            payload.put("variables", variables);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://leetcode.com/graphql"))
                    .timeout(java.time.Duration.ofSeconds(15))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Origin", "https://leetcode.com")
                    .header("Referer", "https://leetcode.com/")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch problem list. HTTP " + response.statusCode());
            }

            JsonNode data = mapper.readTree(response.body())
                    .path("data").path("problemsetQuestionList");
            if (!data.path("questions").isArray()) {
                throw new RuntimeException("Unexpected problem list response from LeetCode.");
            }
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int parseId(JsonNode question) {
        try {
            return Integer.parseInt(question.path("questionFrontendId").asText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static Map<String, Object> fetchGraphQLData(String titleSlug){
        String query = """
            query questionRepo($titleSlug: String!) {
              question(titleSlug: $titleSlug) {
                questionFrontendId
                title
                difficulty
                likes
                dislikes
                topicTags {
                  name
                }
                stats
              }
            }
            """;

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("query", query);
        Map<String, String> variables = new HashMap<>();
        variables.put("titleSlug", titleSlug);
        payloadMap.put("variables", variables);
        
        try{
            String jsonPayload = mapper.writeValueAsString(payloadMap);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://leetcode.com/graphql"))
                    .header("User-Agent", USER_AGENT)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch GraphQL data. HTTP " + response.statusCode());
            }
            
            JsonNode root = mapper.readTree(response.body());
            JsonNode questionNode = root.path("data").path("question");
    
            if (questionNode.isMissingNode() || questionNode.isNull()) {
                throw new RuntimeException("Question data not found.");
            }
    
            String statsString = questionNode.path("stats").asText();
            JsonNode statsNode = mapper.readTree(statsString);
            
            List<String> tags = new ArrayList<>();
            for (JsonNode tagNode : questionNode.path("topicTags")) {
                tags.add(tagNode.path("name").asText());
            }
    
            Map<String, Object> result = new HashMap<>();
            result.put("ID", questionNode.path("questionFrontendId").asText());
            result.put("Title", questionNode.path("title").asText());
            result.put("Difficulty", questionNode.path("difficulty").asText());
            result.put("Likes", questionNode.path("likes").asInt());
            result.put("Dislikes", questionNode.path("dislikes").asInt());
            result.put("TopicTags", tags);
            result.put("AcceptanceRate", statsNode.path("acRate").asText());
            result.put("TotalAccepted", statsNode.path("totalAcceptedRaw").asLong());
            result.put("TotalSubmissions", statsNode.path("totalSubmissionRaw").asLong());
            result.put("TitleSlug",titleSlug);
            
            return result;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> getUserContestRating(String userId) {
        // 1. Update the query to also fetch 'matchedUser'
        String query = """
        query getContestRankingData($username: String!) {
          matchedUser(username: $username) {
            username
          }
          userContestRanking(username: $username) {
            attendedContestsCount
            rating
            globalRanking
            totalParticipants
            topPercentage
          }
        }
        """;

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("query", query);

        Map<String, String> variables = new HashMap<>();
        variables.put("username", userId);
        payloadMap.put("variables", variables);

        try {
            String jsonPayload = mapper.writeValueAsString(payloadMap);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://leetcode.com/graphql"))
                    .header("User-Agent", USER_AGENT)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch contest ranking data. HTTP " + response.statusCode());
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode dataNode = root.path("data");

            JsonNode matchedUserNode = dataNode.path("matchedUser");
            if (matchedUserNode.isMissingNode() || matchedUserNode.isNull()) {
                throw new ResourceNotFoundException("Nope, User with Id '" + userId + "' does not exist.");
            }

            JsonNode rankingNode = dataNode.path("userContestRanking");
            if (rankingNode.isMissingNode() || rankingNode.isNull()) {
                // User is valid, but has never participated in a contest
                return new HashMap<>();
            }

            // 4. User is valid and has contest data
            Map<String, Object> result = new HashMap<>();
            result.put("attendedContestsCount", rankingNode.path("attendedContestsCount").asInt());
            result.put("rating", rankingNode.path("rating").asDouble());
            result.put("globalRanking", rankingNode.path("globalRanking").asInt());
            result.put("totalParticipants", rankingNode.path("totalParticipants").asInt());
            result.put("topPercentage", rankingNode.path("topPercentage").asDouble());

            return result;
        }
        catch(ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}