package com.scrum.parkingapp.data.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PARK_OWNER")
public class Owner extends User{


}
