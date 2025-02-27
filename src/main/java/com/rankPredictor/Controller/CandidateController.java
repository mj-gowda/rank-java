package com.rankPredictor.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rankPredictor.CandidateScraper;
import com.rankPredictor.Model.Candidate;
import com.rankPredictor.Repository.CandidateRepository;
import com.rankPredictor.Response.ApiResponse;
import com.rankPredictor.Service.CandidateService;

@RestController
@RequestMapping("/api/v1/candidate")
public class CandidateController {

  @Autowired
  private CandidateScraper candidateService;

  @Autowired
  private CandidateService candidateService2;

  @Autowired
  private CandidateRepository candidateRepository;

  // @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/scrape")
  public ApiResponse scrapeCandidateData(
      @RequestParam String url,
      @RequestParam String category,
      @RequestParam String state,
      @RequestParam String gender,
      @RequestParam String language) throws IOException {
    return candidateService.scrapeCandidateData(url, category, state, gender, language);
  }

  @GetMapping({ "", "/" })
  public ApiResponse getCandidateById(
      @RequestParam(required = false) String rollNum,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    if (rollNum != null) {
      return candidateService2.getCandidateById(rollNum);
    } else {
      return candidateService2.getAllCandidates(page, size);
    }
  }

  @GetMapping("/rank")
  public ApiResponse getCandidateRank(@RequestParam String rollNum) {
    Candidate candidate = candidateRepository.findById(rollNum).orElse(null);

    return candidateService2.getRankForCandidate(candidate);
  }

  @GetMapping("/rankByCategory")
  public ApiResponse getCandidateByCategoryRank(
      @RequestParam String category,
      @RequestParam String rollNum) {
    Candidate candidate = candidateRepository.findById(rollNum).orElse(null);

    return candidateService2.getRankForCandidateByCategory(candidate, candidate.getCategory());
  }

  @GetMapping("/rankByGender")
  public ApiResponse getCandidateByGenderRank(
      @RequestParam String gender,
      @RequestParam String rollNum) {
    Candidate candidate = candidateRepository.findById(rollNum).orElse(null);

    return candidateService2.getRankForCandidateByGender(candidate, candidate.getGender());
  }

  @GetMapping("/rankByCategoryAndGender")
  public ApiResponse getCandidateByCategoryRankGender(
      @RequestParam String category,
      @RequestParam String rollNum,
      @RequestParam String gender) {
    Candidate candidate = candidateRepository.findById(rollNum).orElse(null);

    return candidateService2.getRankForCandidateByCategoryAndGender(candidate, candidate.getCategory(),
        candidate.getGender());
  }

}
