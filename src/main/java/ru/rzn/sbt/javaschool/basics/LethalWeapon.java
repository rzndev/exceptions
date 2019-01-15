package ru.rzn.sbt.javaschool.basics;

import java.util.concurrent.atomic.AtomicInteger;

public class LethalWeapon {
	
	public LethalWeapon() {
		serial = nextSerial.getAndIncrement();
	}
	
	public LethalWeapon(String color, Double power, int roundsLeft) {
		serial = nextSerial.getAndIncrement();
		this.color = color;
		this.power = power;
		this.roundsLeft = roundsLeft;
	}
	
	public String color;
	private int roundsLeft;
	private Double power;
	
	private static AtomicInteger nextSerial = new AtomicInteger();
	
	private final long serial;
	
	public final long getSerial() {
		return serial;
	}
	
	public Double getPower() {
		return power;
	}
	
	public void setPower(Double power) {
		this.power = power;
	}
	
	public void reload(int cntBullets) {
		roundsLeft += cntBullets;
	}
	
	public void pewPew() {
		roundsLeft -= 2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj.getClass() != getClass()) return false;
		LethalWeapon o = (LethalWeapon)obj;
		if (!o.color.equals(color)) return false;
		if (Math.abs(o.power - power) > Double.MIN_VALUE) return false;
		if (o.roundsLeft != roundsLeft) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = power.hashCode() + 31*roundsLeft + 31*color.hashCode();
		return result;
	}
}
