package com.scrum.parkingapp.config;

import com.scrum.parkingapp.data.entities.*;
import com.scrum.parkingapp.dto.*;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        modelMapper.addMappings(new PropertyMap<PaymentMethodDto, PaymentMethod>() {
            @Override
            protected void configure(){
                skip(destination.getCardNumber());
            }
        });

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
                map(source.getLicensePlate(), destination.getLicencePlate());
                //map(source.getParkingSpotId(), destination.getParkingSpot());
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

        /*
        modelMapper.addMappings(new PropertyMap<ParkingSpaceDto, ParkingSpace>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getUserId().getUserId(), destination.getUser().getId());
                map(source.getAddressDto(), destination.getAddress());
                map(source.getName(), destination.getName());
                map(source.getParkingSpots(), destination.getParkingSpots());

            }
        });*/



        return modelMapper;
    }
}