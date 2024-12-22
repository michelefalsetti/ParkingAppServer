package com.scrum.parkingapp.data.service.implem;


import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.service.ParkingSpotService;
import com.scrum.parkingapp.dto.ParkingSpotDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotDao parkingSpotDao;
    private final ModelMapper modelMapper;


    @Override
    public ParkingSpotDto getById(Long id) {
        return parkingSpotDao.findById(id)
                .map(parkingSpot -> modelMapper.map(parkingSpot, ParkingSpotDto.class))
                .orElse(null);
    }

    @Override
    public ParkingSpotDto save(ParkingSpotDto parkingSpotDto) {
        ParkingSpot parkingSpot = modelMapper.map(parkingSpotDto, ParkingSpot.class);
        System.out.println("Post Mapping: " + parkingSpot);

        ParkingSpot ps = parkingSpotDao.save(parkingSpot);
        return modelMapper.map(ps, ParkingSpotDto.class);
    }

    @Override
    public List<ParkingSpotDto> getByParkingSpaceId(Long parkingSpaceId) {
        return parkingSpotDao.findAllByParkingspaceId(parkingSpaceId)
                .stream()
                .map(parkingSpot -> modelMapper.map(parkingSpot, ParkingSpotDto.class))
                .toList();

    }
}
