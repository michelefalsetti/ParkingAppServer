package com.scrum.parkingapp.config;

import com.scrum.parkingapp.data.entities.*;
import com.scrum.parkingapp.dto.*;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);


        modelMapper.addMappings(new PropertyMap<SaveUserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getCredential().getEmail());
                map(source.getCredential().getPassword(), destination.getCredential().getPassword());
            }
        });

        modelMapper.addMappings(new PropertyMap<User, UserDetailsDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getEmail());
            }
        });

        modelMapper.addMappings(new PropertyMap<Admin, UserDetailsDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getEmail());
            }
        });

        modelMapper.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getBirthDate(), destination.getBirthDate());
            }
        });

        /*
        modelMapper.addMappings(new PropertyMap<PaymentMethodDto, PaymentMethod>() {
            @Override
            protected void configure(){
                skip(destination.getCardNumber());
            }
        });*/

        modelMapper.addMappings(new PropertyMap<RefreshTokenDto, RefreshToken>() {
            @Override
            protected void configure(){
                map(source.getToken(), destination.getToken());
            }
        });




        modelMapper.addMappings(new PropertyMap<ReservationDto, Reservation>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getPrice(), destination.getPrice());
                map(source.getUser(), destination.getUser());
                map(source.getLicensePlate(), destination.getLicensePlate());
                //map(source.getParkingSpotId(), destination.getParkingSpot());
                map(source.getPaymentMethod(), destination.getPaymentMethod());
                map(source.getStartDate(), destination.getStartDate());
                map(source.getEndDate(), destination.getEndDate());

            }
        });

        modelMapper.addMappings(new PropertyMap<AddressDto, Address>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getCity(), destination.getCity());
                map(source.getStreet(), destination.getStreet());
                map(source.getLatitude(), destination.getLatitude());
                map(source.getLongitude(), destination.getLongitude());

            }
        });


        modelMapper.addMappings(new PropertyMap<ParkingSpaceDto, ParkingSpace>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getUserId().getUserId(), destination.getUser().getId());
                map(source.getAddress(), destination.getAddress());
                map(source.getName(), destination.getName());
                map(source.getParkingSpots(), destination.getParkingSpots());

            }
        });

        modelMapper.addMappings(new PropertyMap<ParkingSpotDto, ParkingSpot>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getNumber(), destination.getNumber());
                map(source.getType(), destination.getType());
                map(source.getBasePrice(), destination.getBasePrice());
                map(source.getParkingSpaceId(), destination.getParkingspaceId().getId());
                map(source.getReservations(), destination.getReservations());
            }
        });

        modelMapper.addMappings(new PropertyMap<ParkingSpot, ParkingSpotDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getNumber(), destination.getNumber());
                map(source.getBasePrice(), destination.getBasePrice());
                map(source.getParkingspaceId().getId(), destination.getParkingSpaceId());
                map(source.getReservations(), destination.getReservations());
            }
        });



        return modelMapper;
    }


}