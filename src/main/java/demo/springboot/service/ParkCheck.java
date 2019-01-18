package demo.springboot.service;

import demo.springboot.domain.ParkResult;

public interface ParkCheck {
    public ParkResult getParkResult(byte [] data);
}
