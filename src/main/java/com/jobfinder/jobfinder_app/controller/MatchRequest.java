package com.jobfinder.jobfinder_app.controller;

public record MatchRequest(String cvText, String title, String location, Integer daysPosted) {}