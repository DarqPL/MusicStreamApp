package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.SearchResultDTO;
import iuh.fit.MusicStreamAppBackEnd.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Endpoint tìm kiếm toàn cục.
     * Công khai.
     * Ví dụ: /api/search?q=mylove
     */
    @GetMapping
    public ResponseEntity<SearchResultDTO> search(@RequestParam("q") String query) {
        SearchResultDTO results = searchService.search(query);
        return ResponseEntity.ok(results);
    }
}