package com.rankPredictor.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "CANDIDATES")
public class Candidate {

  @Id
  private String rollNumber;

  private String name;
  private Date examDate;
  private String examTime;
  private String venueName;
  private String url;
  private String subject;
  // private Integer candidateRank;
  private Double totalMarks;

  @OneToMany(mappedBy = "candidate")
  private List<Section> sections;

  private String category;
  private String state;
  private String gender;
  private Integer totalAttempted;
  private Integer totalRight;
  private Integer totalWrong;
  private Integer totalNotAttempted;
  private String language;

  public void calculateTotalMarks() {
    if (sections != null) {
      this.totalMarks = sections.stream().mapToDouble(Section::getTotalMarks).sum();
    } else {
      this.totalMarks = 1.0;
    }
  }
}
