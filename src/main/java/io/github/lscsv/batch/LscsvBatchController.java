package io.github.lscsv.batch;

import io.github.lscsv.util.LogMessageSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/lscv")
public class LscsvBatchController {

    private final LscsvBatchService service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public LscsvBatchController(LscsvBatchService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LscsvBatchService.JobInfo> findLscsv(
            @PathVariable(name = "id") Long executionId) {
        LscsvBatchService.JobInfo info = service.findLscsvJob(executionId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/launch")
    public ResponseEntity<LscsvBatchService.JobInfo> launchLscsvTest(
            @RequestParam(name = "file") String file) {
        logger.info("file provided:" +  LogMessageSanitizer.sanitize(file));
        return ResponseEntity.ok(service.lanchLscsv(file));
    }

    @PostMapping
    public ResponseEntity<LscsvBatchService.JobInfo> launchLscsv(
            @RequestParam(name = "file") String file) {
        return ResponseEntity.ok(service.lanchLscsv(file));
    }

    @PostMapping("/dummy")
    public ResponseEntity<LscsvBatchService.LscsvInfo> apiDummyLscsv(@RequestBody LscsvBatchService.LscsvInfo info) {
        return ResponseEntity.ok(info);
    }

    @GetMapping("/launch-test")
    public ResponseEntity<LscsvBatchService.JobInfo> launchLscsvTestLog(
            @RequestBody MultipartFile file, @RequestParam String search, @RequestParam String metaData) {

        logger.info("Starting launch for file:{}, with search: {}, and metaData: {} ",
                LogMessageSanitizer.sanitizeNew(file.getOriginalFilename()), 
                LogMessageSanitizer.sanitizeNew(search), 
                LogMessageSanitizer.sanitizeNew(metaData));

        return ResponseEntity.ok(service.lanchLscsv(file.getOriginalFilename()));
    }
}
