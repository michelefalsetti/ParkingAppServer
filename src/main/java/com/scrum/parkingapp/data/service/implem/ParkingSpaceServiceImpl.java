package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final UsersDao usersDao;

    private final ParkingSpaceDao parkingSpaceDao;

    private final ModelMapper modelMapper;


    @Override
    public ParkingSpaceDto getById(Long id) {
        return parkingSpaceDao.findById(id)
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDto.class))
                .orElse(null);
    }

    @Override
    public ParkingSpaceDto save(ParkingSpaceDto parkingSpaceDto) {
        System.out.println("ParkingSpaceDto: " + parkingSpaceDto);
        ParkingSpace parkingSpace = modelMapper.map(parkingSpaceDto, ParkingSpace.class);
        System.out.println("Post Mapping: " + parkingSpace);
        ParkingSpace ps = parkingSpaceDao.save(parkingSpace);

        return modelMapper.map(ps, ParkingSpaceDto.class);
    }

    @Override
    public List<ParkingSpaceDto> getAllDto() {
        return parkingSpaceDao.findAll().stream()
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpaceDto> getAllByOwnerId(UUID ownerId) {
         return parkingSpaceDao.findAllByUserId(ownerId).stream()
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDto.class))
                .collect(Collectors.toList());
    }



}
