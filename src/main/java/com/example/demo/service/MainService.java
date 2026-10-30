package com.example.demo.service;

import com.example.demo.dto.Data;
import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Double.parseDouble;

@Service
@EnableCaching
public class MainService {
    @Autowired
    @Lazy
    private MainService self;

    private final QuestionRepo questionRepo;
    private final LeetcodeScraperService leetcodeScraperService;

    MainService(QuestionRepo questionRepo, LeetcodeScraperService leetcodeScraperService) {
        this.questionRepo = questionRepo;
        this.leetcodeScraperService = new LeetcodeScraperService(questionRepo);
    }

    private String convertListtoString(List<String> topiclist){
        if(topiclist.isEmpty()){
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String topic:topiclist){
            sb.append(topic).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }

    public Data getDetails(int id){
        Map<String,Object> mp = leetcodeScraperService.getLeetCodeData(id);

        List<String> topiclist = (List<String>) mp.get("TopicTags");

        Data outputData = new Data();
        outputData.setId((String)mp.get("ID"));
        outputData.setTitle((String)mp.get("Title"));
        outputData.setDifficulty((String)mp.get("Difficulty"));
        outputData.setAcceptanceRate((String)mp.get("AcceptanceRate"));
        outputData.setDislikes(String.valueOf(mp.get("Dislikes")));
        outputData.setLikes(String.valueOf(mp.get("Likes")));
        outputData.setTotalAccepted(String.valueOf(mp.get("TotalAccepted")));
        outputData.setTotalSubmissions(String.valueOf(mp.get("TotalSubmissions")));
        outputData.setTags(topiclist);

        int rating = self.getRating(mp,(String)mp.get("ID"),topiclist);
        outputData.setRating(Integer.toString(rating));

        return outputData;
    }

    @Cacheable(value = "Ratings",key = "#id")
    public int getRating(Map<String,Object> mp,String id,List<String> topiclist){

        Optional<Integer> rating = questionRepo.getQuestionRatingById(Integer.parseInt(id));
        if(rating.isPresent()){
            return rating.get();
        }

        String topicString = convertListtoString(topiclist);
        String query = mp.get("ID") + "," + mp.get("Likes") + "," + mp.get("Dislikes") +
                "," + mp.get("Difficulty") + "," + mp.get("AcceptanceRate") +
                "," + mp.get("TotalAccepted") + "," + mp.get("TotalSubmissions") +
                "," + topicString;

        String predictedRating = predictValue(query);
        double rating_num = parseDouble(predictedRating);
        int rating_in_int = (int) rating_num;

        Question question = new Question();
        question.setId(Integer.parseInt(id));
        question.setRating(rating_in_int);
        question.setTitle((String) mp.get("Title"));
        question.setTitleSlug((String) mp.get("TitleSlug"));

        questionRepo.save(question);
        return rating_in_int;
    }

    public String predictValue(String query){
        try{
            ProcessBuilder pb = new ProcessBuilder("python3","ml_requirements/mlPredictor.py",query);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            String result = new String(p.getInputStream().readAllBytes()).trim();

            int exitcode = p.waitFor();
            if(exitcode != 0){
                return "Python Crashed. Log:\n" + result;
            }
            return result;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
