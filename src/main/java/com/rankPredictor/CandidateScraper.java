package com.rankPredictor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.rankPredictor.Model.Candidate;
import com.rankPredictor.Model.Section;
import com.rankPredictor.Repository.CandidateRepository;
import com.rankPredictor.Repository.SectionRepository;
import com.rankPredictor.Response.ApiResponse;

@Component
public class CandidateScraper {

  @Autowired
  private SectionRepository sectionRepository;

  @Autowired
  private CandidateRepository candidateRepository;

  public ApiResponse scrapeCandidateData(
      String url,
      String category,
      String state,
      String gender, String language) throws IOException {
    Candidate existingCandidate = candidateRepository.findByUrl(url);
    if (existingCandidate != null) {
      return new ApiResponse(
          HttpStatus.FOUND,
          "Candidate data saved Successfully",
          existingCandidate,
          true);
    }

    Document doc = Jsoup.connect(url).get();
    Candidate candidate = new Candidate();
    candidate.setUrl(url);
    candidate.setCategory(category);
    candidate.setGender(gender);
    candidate.setState(state);
    candidate.setLanguage(language);

    Element rollNumberElement = doc
        .select("table tbody tr td:contains(Roll Number)")
        .first()
        .nextElementSibling();
    String rollNumber = rollNumberElement.text();

    Candidate rollNmbr = candidateRepository.findById(rollNumber).orElse(null);
    if (rollNmbr != null) {
      return new ApiResponse(
          HttpStatus.FOUND,
          "Candidate data retireived Successfully",
          rollNmbr,
          true);
    }

    Element nameElement = doc
        .select("table tbody tr td:contains(Candidate Name)")
        .first()
        .nextElementSibling();
    String name = nameElement.text();

    Element venueElement = doc
        .select("table tbody tr td:contains(Venue Name)")
        .first()
        .nextElementSibling();
    String venue = venueElement.text();

    Element examDateElement = doc
        .select("table tbody tr td:contains(Exam Date)")
        .first()
        .nextElementSibling();
    Date examDate = parseDate(examDateElement.text());

    Element examTimeElement = doc
        .select("table tbody tr td:contains(Exam Time)")
        .first()
        .nextElementSibling();
    String examTime = examTimeElement.text();

    Element subjectElement = doc
        .select("table tbody tr td:contains(Subject)")
        .first()
        .nextElementSibling();
    String subject = subjectElement.text();

    // updateCandidatesTotalMarks();
    // updateRanks();
    Candidate candidate2 = scrapeAndSaveSections(
        doc,
        candidate,
        rollNumber,
        name,
        venue,
        examDate,
        examTime,
        subject);

    return new ApiResponse(
        HttpStatus.FOUND,
        "Candidate data saved Successfully",
        candidate2,
        true);
  }

  private Date parseDate(String dateStr) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      return dateFormat.parse(dateStr);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Error parsing date: " + dateStr, e);
    }
  }

  private Candidate scrapeAndSaveSections(
      Document doc,
      Candidate candidate,
      String rollNumber,
      String name,
      String venue,
      Date examDate,
      String examTime,
      String subject) {
    Candidate existingCandidate = candidateRepository
        .findById(rollNumber)
        .orElse(null);
    if (existingCandidate != null) {
      return existingCandidate;
    }
    Double totalMarks = 0.0;
    Double marks = 0.0;
    Integer totalRight = 0;
    Integer totalWrong = 0;
    Integer totalAttempted = 0;
    Integer totalNotAttempted = 0;

    Elements sec = doc.select("div.section-cntnr");
    // Elements sectionNames = sec.select(
    // "div.section-cntnr section-lbl:contains(Section :)");
    // String sectNm = sectionNames.text();
    for (int k = 0; k < sec.size(); k++) {
      Element srow = sec.get(k);
      Section section = new Section();
      Integer right = 0;
      Integer wrong = 0;
      Integer attempted = 0;
      Integer notAttempted = 0;

      Element sectionLabel = srow.selectFirst("div.section-lbl span.bold");
      String sectNm = sectionLabel != null ? sectionLabel.text() : "Unknown Section";

      Elements qustn = srow.select("div.question-pnl");
      for (int j = 0; j < qustn.size(); j++) {
        Element qrow = qustn.get(j);
        Elements rows = qrow.select(
            "table tbody tr td table.questionRowTbl tbody tr");

        Element answered = qrow
            .select("table tbody tr td table.menu-tbl tbody tr td:contains(Chosen Option)")
            .first()
            .nextElementSibling();

        String ans = "";

        for (int i = 0; i < rows.size(); i++) {
          Element row = rows.get(i);
          Element rightAnsCell = row.selectFirst("td.rightAns");
          if (rightAnsCell != null) {
            ans = "" + Math.abs(((rows.size() - (i + 1)) % 4) - 4);
            break;
          }
        }
        System.out.println(answered.text());
        if (!answered.text().equals("--")) {
          if (answered.text().equals(ans)) {
            right++;
          } else {
            wrong++;
          }
          attempted++;
        } else {
          notAttempted++;
        }
      }
      if (candidate.getRollNumber() == null) {
        candidate.setRollNumber(rollNumber);
        candidate.setExamDate(examDate);
        candidate.setExamTime(examTime);
        candidate.setName(name);
        candidate.setVenueName(venue);
        candidate.setSubject(subject);
        candidateRepository.save(candidate);
      }
      section.setRight_answered(right);
      section.setAttempted(attempted);
      if (notAttempted == 20) {
        totalMarks = 0.0;
      }
      section.setNotAttempted(notAttempted);
      section.setWrong_answered(wrong);
      section.setSectionName(sectNm);
      section.setCandidate(candidate);

      totalMarks += ((right * 2) - (wrong * 0.25));
      section.setTotalMarks(totalMarks);
      marks += totalMarks;
      totalMarks = 0.0;
      totalAttempted += attempted;
      totalRight += right;
      totalWrong += wrong;
      totalNotAttempted += notAttempted;

      sectionRepository.save(section);
    }
    candidate.setTotalMarks(marks);
    // candidate.setTotalSectionStats(totalSectionStats);
    candidate.setTotalRight(totalRight);
    candidate.setTotalAttempted(totalAttempted);
    candidate.setTotalNotAttempted(totalNotAttempted);
    candidate.setTotalWrong(totalWrong);
    candidateRepository.save(candidate);

    return candidate;
  }
}
