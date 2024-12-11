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

        /*
        modelMapper.addMappings(new PropertyMap<LicensePlateDto, LicensePlate>() {
            @Override
            protected void configure() {
                map(source.get, destination.getLpNumber());
                map(source.getDriver(), destination.getDriver());
            }
        });*/


        modelMapper.addMappings(new PropertyMap<ReservationDto, Reservation>() {
            @Override
            protected void configure() {
                /*
                System.out.println("ID: " + source.getId() + " " + destination.getId());
                map(source.getId(), destination.getId());*/

                map(source.getDriverId(), destination.getDriver().getId());

                map(source.getLicensePlateId() , destination.getLicensePlate().getDriver().getId());

                map(source.getParkingSpotId() , destination.getParkingSpot().getId());

                /*
                System.out.println("ParkingSpot ID: " + source.getParkingSpot() + " " + destination.getParkingSpot());
                map(source.getParkingSpot().getId(), destination.getParkingSpot().getId() );

                System.out.println("Price: " + source.getPrice() + " " + destination.getPrice());
                map(source.getPrice(), destination.getPrice());

                System.out.println("LicensePlate: " + source.getLicensePlate() + " " + destination.getLicensePlate());
                map(source.getLicensePlate().getId(), destination.getLicensePlate().getLicensePlateId());
                map(source.getStartDate(), destination.getStartDate());
                map(source.getEndDate(), destination.getEndDate());*/
            }
        });


        return modelMapper;
    }
}