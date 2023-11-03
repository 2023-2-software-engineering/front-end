package com.example.festival.partner.controller;

import com.example.festival.partner.dto.PartnerDto;
import com.example.festival.partner.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(
            @Autowired PartnerService partnerService
    ) {
        this.partnerService = partnerService;
    }

    @PostMapping("")
    public void partnerCreate(@RequestBody PartnerDto partnerDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 로그인한 사용자 정보
        this.partnerService.partnerCreate(authentication.getName(), partnerDto);
    }

    @GetMapping("/{partnerId}")
    public PartnerDto partnerRead(@PathVariable("partnerId") Long partnerId) {
        return this.partnerService.partnerRead(partnerId);
    }

    @GetMapping("")
    public List<PartnerDto> partnerReadAll() {
        return this.partnerService.partnerReadAll();
    }

    @PatchMapping("/{partnerId}")
    public void partnerUpdate(@PathVariable("partnerId") Long partnerId, @RequestBody PartnerDto partnerDto) {
        this.partnerService.partnerUpdate(partnerId, partnerDto);
    }

    @DeleteMapping("/{partnerId}")
    public void partnerDelete(@PathVariable("partnerId") Long partnerId) {
        this.partnerService.partnerDelete(partnerId);
    }
}
