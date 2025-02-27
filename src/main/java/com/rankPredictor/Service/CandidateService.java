package com.rankPredictor.Service;

import com.rankPredictor.Model.Candidate;
import com.rankPredictor.Repository.CandidateRepository;
import com.rankPredictor.Response.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

  @Autowired
  private CandidateRepository candidateRepository;

  public ApiResponse getCandidateById(String rollNum) {
    return new ApiResponse(
        HttpStatus.FOUND,
        "Candidate found Successfully",
        candidateRepository.findById(rollNum),
        true);
  }

  public ApiResponse getAllCandidates(int page, int size) {
    Pageable paging = PageRequest.of(page, size);
    Page<Candidate> data = candidateRepository.findAllOrderByTotalMarksDesc(
      paging
    );
 
    Map<String, Object> response = new HashMap<>();
    response.put("data", data.getContent());
    response.put("currentPage", data.getNumber());
    response.put("totalItems", data.getTotalElements());
    response.put("totalPages", data.getTotalPages());
 
    return new ApiResponse(
      HttpStatus.FOUND,
      "Got Successfully",
      response,
      true
    );
  }

  public ApiResponse getRankForCandidate(Candidate candidate) {
    List<Candidate> candidates = candidateRepository.findAllOrderByTotalMarksDesc();

    int rank = 1;
    for (Candidate c : candidates) {
      if (c.getTotalMarks() > candidate.getTotalMarks()) {
        rank++;
      } else {
        break;
      }
    }

    Map<String, String> map = new HashMap<>();
    map.put("rank", "" + rank);
    map.put("total", "" + candidateRepository.count());

    return new ApiResponse(
        HttpStatus.FOUND,
        "Candidates rank found Successfully",
        map,
        true);
  }

  public ApiResponse getRankForCandidateByCategory(
      Candidate candidate,
      String category) {
    List<Candidate> candidatesByCategory = candidateRepository.findAllByCategoryOrderByTotalMarksDesc(
        category);

    int rank = 1;
    for (Candidate c : candidatesByCategory) {
      if (c.getTotalMarks() > candidate.getTotalMarks()) {
        rank++;
      } else {
        break;
      }
    }

    Map<String, String> map = new HashMap<>();
    map.put("rank", "" + rank);
    map.put("total", "" + candidateRepository.countByCategory(category));

    return new ApiResponse(
        HttpStatus.FOUND,
        "Candidates rank found Successfully",
        map,
        true);
  }
  // public ApiResponse getAvgMarksByCandidate(String rollNum) {
  // Candidate candidate = candidateRepository.findById(rollNum).orElse(null);
  // if (candidate == null) {
  // return new ApiResponse(
  // HttpStatus.NOT_FOUND,
  // "Candidate not found",
  // null,
  // false
  // );
  // }

  public ApiResponse getRankForCandidateByGender(Candidate candidate, String gender) {
    List<Candidate> candidatesByCategory = candidateRepository.findAllByGenderOrderByTotalMarksDesc(
        gender);

    int rank = 1;
    for (Candidate c : candidatesByCategory) {
      if (c.getTotalMarks() > candidate.getTotalMarks()) {
        rank++;
      } else {
        break;
      }
    }

    Map<String, String> map = new HashMap<>();
    map.put("rank", "" + rank);
    map.put("total", "" + candidateRepository.countByGender(gender));

    return new ApiResponse(
        HttpStatus.FOUND,
        "Candidates rank found Successfully",
        map,
        true);
  }

  public ApiResponse getRankForCandidateByCategoryAndGender(
    Candidate candidate,
    String category,
    String gender) {
  List<Candidate> candidatesByCategory = candidateRepository.findAllByCategoryAndGenderOrderByTotalMarksDesc(
      category,gender);

  int rank = 1;
  for (Candidate c : candidatesByCategory) {
    if (c.getTotalMarks() > candidate.getTotalMarks()) {
      rank++;
    } else {
      break;
    }
  }

  Map<String, String> map = new HashMap<>();
  map.put("rank", "" + rank);
  map.put("total", "" + candidateRepository.countByCategoryAndGender(category,gender));

  return new ApiResponse(
      HttpStatus.FOUND,
      "Candidates rank found Successfully",
      map,
      true);
}
}
