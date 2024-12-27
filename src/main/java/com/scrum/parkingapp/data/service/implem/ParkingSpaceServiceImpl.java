package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.AddressDao;
import com.scrum.parkingapp.data.dao.LicensePlateDao;
import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.Address;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final UsersDao usersDao;

    private final ParkingSpaceDao parkingSpaceDao;

    private final AddressDao addressDao;

    private final LicensePlateDao licensePlateDao;

    private final ModelMapper modelMapper;


    @Override
    public ParkingSpaceDto getById(Long id) {
        return parkingSpaceDao.findById(id)
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDto.class))
                .orElse(null);
    }


    @Override
    public List<ParkingSpaceDto> findAllByCityAndStartDateAndEndDate(String city, LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = parkingSpaceDao.findParkingSpacesAndAvailableSpots(city, startDate, endDate);

        Map<ParkingSpace, List<ParkingSpot>> spaceToSpots = new HashMap<>();

        for (Object[] result : results) {
            ParkingSpace space = (ParkingSpace) result[0];
            ParkingSpot spot = (ParkingSpot) result[1];

            /*
            System.out.println("Space: " + space.toString());
            System.out.println("Spot: " + spot.toString());*/

            spaceToSpots.computeIfAbsent(space, k -> new ArrayList<>()).add(spot);
        }

        return spaceToSpots.entrySet().stream()
                .map(entry -> toParkingSpaceDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private ParkingSpaceDto toParkingSpaceDto(ParkingSpace space, List<ParkingSpot> spots) {
        ParkingSpaceDto dto = modelMapper.map(space, ParkingSpaceDto.class);
        dto.setParkingSpots(spots.stream().map(spot -> modelMapper.map(spot, ParkingSpotDto.class)).toList());
        return dto;
    }


    @Override
    @Transactional
    public ParkingSpaceDto save(ParkingSpaceDto parkingSpaceDto) {
        System.out.println("ParkingSpaceDto: " + parkingSpaceDto);

        // Validazione indirizzo
        if (parkingSpaceDto.getAddress() == null) {
            throw new IllegalArgumentException("Address is required");
        }

        Address address;

        // Verifica se l'indirizzo è nuovo o esistente
        if (parkingSpaceDto.getAddress().getId() == null) {
            address = new Address();
            address.setCity(parkingSpaceDto.getAddress().getCity());
            address.setStreet(parkingSpaceDto.getAddress().getStreet());
            address.setLatitude(parkingSpaceDto.getAddress().getLatitude());
            address.setLongitude(parkingSpaceDto.getAddress().getLongitude());

            // Salva il nuovo indirizzo
            address = addressDao.save(address);
        } else {
            // Carica l'indirizzo esistente dal database
            address = addressDao.findById(parkingSpaceDto.getAddress().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        }

        // Mappa il DTO a Entity
        ParkingSpace parkingSpace = modelMapper.map(parkingSpaceDto, ParkingSpace.class);

        // Associa l'indirizzo all'entità ParkingSpace
        parkingSpace.setAddress(address);

        // Salva il parking space
        ParkingSpace savedParkingSpace = parkingSpaceDao.save(parkingSpace);

        // Mappa l'entità salvata di nuovo a DTO e restituiscila
        return modelMapper.map(savedParkingSpace, ParkingSpaceDto.class);
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

    @Override
    public List<LicensePlateDto> getLicensePlates(UUID idUser) {
        return licensePlateDao.findAllByUserId(idUser).stream()
                .map(licensePlate -> modelMapper.map(licensePlate, LicensePlateDto.class))
                .collect(Collectors.toList());
    }


}
