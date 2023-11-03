package com.example.festival.partner.service;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.partner.dto.PartnerDto;
import com.example.festival.partner.entity.Partner;
import com.example.festival.partner.repository.PartnerRepository;
import com.example.festival.user.entity.User;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final AuthRepository authRepository;

    public PartnerService(@Autowired PartnerRepository partnerRepository,
                          @Autowired AuthRepository authRepository) {
        this.partnerRepository=partnerRepository;
        this.authRepository = authRepository;
    }

    public void partnerCreate(String identify, PartnerDto partnerDto) {
        //User user = authRepository.findByIdentify(identify);
        partnerRepository.partnerCreate(identify, partnerDto);
    }

    public PartnerDto partnerRead(Long partnerId) {
        Partner partner = this.partnerRepository.partnerRead(partnerId); //읽을 게시물을 찾음
        PartnerDto partnerDto = new PartnerDto();
        BeanUtils.copyProperties(partner, partnerDto);

        partnerDto.setNickname(partner.getUser().getNickname()); // DTO에 user nickname을 넣음
        partnerDto.setAddress(partner.getUser().getAddress()); //DTO에 user address를 넣음
        partnerDto.setCount(this.partnerRepository.countCommentsAndReplies(partnerId));
        return partnerDto;
    }

    public List<PartnerDto> partnerReadAll() {
        List<PartnerDto> partnerDtoList = new ArrayList<>();
        Iterator<Partner> iterator = this.partnerRepository.partnerReadAll();

        while(iterator.hasNext()) {
            PartnerDto partnerDto = partnerRead(iterator.next().getPartnerId());
            partnerDtoList.add(partnerDto);
        }
        return partnerDtoList;
    }

    public void partnerUpdate(Long partnerId, PartnerDto partnerDto) {
        this.partnerRepository.partnerUpdate(partnerId, partnerDto);
    }


    public void partnerDelete(Long partnerId) {
        this.partnerRepository.partnerDelete(partnerId);
    }

}
