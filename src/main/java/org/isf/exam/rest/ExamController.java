package org.isf.exam.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exam.dto.ExamDTO;
import org.isf.exam.mapper.ExamMapper;
import org.isf.exatype.manager.ExamTypeBrowserManager;
import org.isf.exatype.model.ExamType;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE, authorizations = {@Authorization(value = "basicAuth")})
public class ExamController {

    private final Logger logger = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    protected ExamBrowsingManager examManager;
    @Autowired
    protected ExamTypeBrowserManager examTypeBrowserManager;
    @Autowired
    private ExamMapper examMapper;

    public ExamController(ExamBrowsingManager examManager, ExamMapper examMapper) {
        this.examManager = examManager;
        this.examMapper = examMapper;
    }

    @PostMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity newExam(@RequestBody ExamDTO newExam) throws OHServiceException {
        ExamType examType = examTypeBrowserManager.getExamType().stream().filter(et -> newExam.getExamtype().getCode().equals(et.getCode())).findFirst().orElse(null);

        if (examType == null) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam type not found!", OHSeverityLevel.ERROR));
        }

        Exam exam = examMapper.map2Model(newExam);
        exam.setExamtype(examType);

        boolean isCreated = examManager.newExam(exam);
        if (!isCreated) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam is not created!", OHSeverityLevel.ERROR));
        }
        return ResponseEntity.ok(exam.getCode());
    }

    @PutMapping(value = "/exams/{code:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateExams(@PathVariable String code, @RequestBody ExamDTO updateExam) throws OHServiceException {

        if (!updateExam.getCode().equals(code)) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam code mismatch", OHSeverityLevel.ERROR));
        }
        if (examManager.getExams().stream().noneMatch(e -> e.getCode().equals(code))) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam not Found!", OHSeverityLevel.WARNING));
        }

        ExamType examType = examTypeBrowserManager.getExamType().stream().filter(et -> updateExam.getExamtype().getCode().equals(et.getCode())).findFirst().orElse(null);
        if (examType == null) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam type not found!", OHSeverityLevel.ERROR));
        }

        Exam exam = examMapper.map2Model(updateExam);
        exam.setExamtype(examType);
        if (!examManager.updateExam(exam)) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam is not updated!", OHSeverityLevel.ERROR));
        }

        return ResponseEntity.ok(true);
    }


    @GetMapping(value = "/exams/description/{description:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExamDTO>> getExams(@PathVariable String description) throws OHServiceException {
        List<ExamDTO> exams = examMapper.map2DTOList(examManager.getExams(description));

        if (exams == null || exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.ok(exams);
        }
    }

    @GetMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExamDTO>> getExams() throws OHServiceException {
        List<ExamDTO> exams = examMapper.map2DTOList(examManager.getExams());

        if (exams == null || exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.ok(exams);
        }
    }

    @DeleteMapping(value = "/exams/{code:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteExam(@PathVariable String code) throws OHServiceException {
        Optional<Exam> exam = examManager.getExams().stream().filter(e -> e.getCode().equals(code)).findFirst();
        if (!exam.isPresent()) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam not Found!", OHSeverityLevel.WARNING));
        }
        if (!examManager.deleteExam(exam.get())) {
            throw new OHAPIException(new OHExceptionMessage(null, "Exam is not deleted!", OHSeverityLevel.ERROR));
        }
        return ResponseEntity.ok(true);
    }
}
