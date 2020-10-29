package com.capgemini.iplleagueanalysis;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IPLAnalyserTest {

	private final String PLAYER_RUNS_DATA = "C:\\Users\\MAHI\\eclipse-workspace\\iplLeagueAnalysis\\src\\main\\resources\\WP DP Data_01 IPL2019FactsheetMostRuns.csv";

	IPLAnalyser iplAnalyser = null;

	@Before
	public void createIplAnalyserObject() {
		iplAnalyser = new IPLAnalyser();
	}

	@Test
	public void givenCsvDataShouldReturnTopBattingAvg() {
		try {
			iplAnalyser.loadRunsData(PLAYER_RUNS_DATA);
			String playerName = iplAnalyser.getTopBattingAvg();
			assertEquals("MS Dhoni", playerName);
		} catch (IPLAnalyserException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givenCsvDataShouldReturnTopStrikeRatePlayer() {
		try {
			iplAnalyser.loadRunsData(PLAYER_RUNS_DATA);
			String playerName = iplAnalyser.getTopStrikeRate();
			assertEquals("Ishant Sharma", playerName);
		} catch (IPLAnalyserException e) {
			e.printStackTrace();
		}
	}

}