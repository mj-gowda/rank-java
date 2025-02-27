package com.rankPredictor.Repository;

import com.rankPredictor.Model.Candidate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
  Candidate findByUrl(String url);

  @Query("SELECT c FROM Candidate c ORDER BY c.totalMarks DESC")
  Page<Candidate> findAllOrderByTotalMarksDesc(Pageable paging);

  @Query("SELECT c FROM Candidate c ORDER BY c.totalMarks DESC")
  List<Candidate> findAllOrderByTotalMarksDesc();

  List<Candidate> findAllByCategoryOrderByTotalMarksDesc(String category);

  List<Candidate> findAllByGenderOrderByTotalMarksDesc(String gender);

  // @Query(
  // "SELECT AVG(c.totalMarks) FROM Candidate c WHERE c.rollNumber = :rollNumber"
  // )
  // Double findAverageTotalMarksByRollNumber(String rollNumber);

  String countByCategory(String category);

  String countByCategoryAndGender(String category, String gender);

  String countByGender(String gender);

  // @Query(
  // "SELECT AVG(c.totalMarks) FROM Candidate c WHERE c.rollNumber = :rollNumber
  // and c.category = :category"
  // )
  // Double findAverageTotalMarksByRollCategory(
  // String rollNumber,
  // String category
  // );

  List<Candidate> findAllByCategoryAndGenderOrderByTotalMarksDesc(String category, String gender);
  

  
}
