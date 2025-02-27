package com.rankPredictor.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "Sections")
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long secId;

  private Integer right_answered;
  private Integer wrong_answered;
  private Integer attempted;
  private Integer notAttempted;
  private Double totalMarks;
  private String sectionName;

  @ManyToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "ROLL_NUM")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Candidate candidate;
}
