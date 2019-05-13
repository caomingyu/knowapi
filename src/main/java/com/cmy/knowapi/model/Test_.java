package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
public class Test_ {
    private List<User> users;
}
