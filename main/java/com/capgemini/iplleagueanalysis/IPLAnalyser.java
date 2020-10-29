package com.capgemini.iplleagueanalysis;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.capgemini.csvbuilder.BuilderException;
import com.capgemini.csvbuilder.CsvBuilderFactory;
import com.capgemini.csvbuilder.ICsvBuilder;

public class IPLAnalyser {

	List<IPLBatting> playerRunsList = null;
	private Comparator<IPLBatting> runsComparator;
	private List<IPLBowling> bowlerDataList = null;
	private Comparator<IPLBowling> bowlerComparator;

	public void loadRunsData(String filePath) throws IPLAnalyserException {
		try {
			Reader reader = Files.newBufferedReader(Paths.get(filePath));
			new CsvBuilderFactory();
			ICsvBuilder csvBuilderCustom = CsvBuilderFactory.createBuilderCommons();

			playerRunsList = csvBuilderCustom.getCSVFileList(reader, IPLBatting.class);

		} catch (IOException e) {
			throw new IPLAnalyserException(e.getMessage(), IPLAnalyserException.Exception.INCORRECT_FILE);
		} catch (BuilderException e) {
			throw new IPLAnalyserException(e.getMessage(), e.type.name());
		}

	}

	public void loadWktsData(String filePath) throws IPLAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));) {
			new CsvBuilderFactory();
			ICsvBuilder csvBuilderCustom = CsvBuilderFactory.createBuilderCommons();

			bowlerDataList = csvBuilderCustom.getCSVFileList(reader, IPLBowling.class);

		} catch (IOException e) {
			throw new IPLAnalyserException(e.getMessage(), IPLAnalyserException.Exception.INCORRECT_FILE);
		} catch (BuilderException e) {
			throw new IPLAnalyserException(e.getMessage(), e.type.name());
		}

	}

	public String getTopBattingAvg() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(IPLBatting::getAverage);
		return getBatsmanName();
	}

	public String getTopStrikeRate() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(s -> s.strikeRate);
		return getBatsmanName();
	}

	public String getMaximum6sAnd4s() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(s -> s.sixes + s.fours);
		return getBatsmanName();
	}

	public String getBestStrickRateMaximum6sAnd4s() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(s -> s.sixes + s.fours);
		runsComparator = runsComparator.thenComparing(s -> s.strikeRate);
		return getBatsmanName();
	}

	public String getGreatAvgwithBestStrickRate() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(IPLBatting::getAverage).thenComparing(s -> s.strikeRate);
		return getBatsmanName();
	}

	public String getMaxRunsWithBestAvg() throws IPLAnalyserException {
		checkForData();
		runsComparator = Comparator.comparing(s -> s.runs);
		runsComparator = runsComparator.thenComparing(IPLBatting::getAverage);
		return getBatsmanName();
	}

	public String getTopBowlingAvg() throws IPLAnalyserException {
		checkForBowlerData();
		bowlerComparator = Comparator.comparing(IPLBowling::getAverage);
		return getBowlerName();
	}

	public String getTopBowlingStrakeRate() throws IPLAnalyserException {
		checkForBowlerData();
		bowlerComparator = Comparator.comparing(IPLBowling::getStrikeRate);
		return getBowlerName();
	}

	public String getBestEconomy() throws IPLAnalyserException {
		checkForBowlerData();
		bowlerComparator = Comparator.comparing(s -> s.economy);
		return getBowlerName();
	}

	public String getBestStrikeRateWith4w5w() throws IPLAnalyserException {
		checkForBowlerData();
		bowlerComparator = Comparator.comparing(s -> s.fourWickets + s.fiveWickets);
		bowlerComparator = bowlerComparator.reversed();
		bowlerComparator = bowlerComparator.thenComparing(IPLBowling::getStrikeRate);
		return getBowlerName();
	}

	private String getBatsmanName() {
		this.sortBatsmenData(runsComparator);
		Collections.reverse(playerRunsList);
		return playerRunsList.get(0).player;
	}

	private void checkForData() throws IPLAnalyserException {
		if (playerRunsList == null || playerRunsList.size() == 0) {
			throw new IPLAnalyserException("No Census Data", IPLAnalyserException.Exception.NO_CENSUS_DATA);
		}
	}

	private String getBowlerName() {
		this.sortBowlerData(bowlerComparator);
		return bowlerDataList.get(0).player;
	}

	private void checkForBowlerData() throws IPLAnalyserException {
		if (bowlerDataList == null || bowlerDataList.size() == 0) {
			throw new IPLAnalyserException("No Census Data", IPLAnalyserException.Exception.NO_CENSUS_DATA);
		}
	}

	private void sortBatsmenData(Comparator<IPLBatting> comparator) {
		for (int i = 0; i < playerRunsList.size() - 1; i++) {
			for (int j = 0; j < playerRunsList.size() - i - 1; j++) {
				IPLBatting census1 = playerRunsList.get(j);
				IPLBatting census2 = playerRunsList.get(j + 1);
				if (comparator.compare(census1, census2) > 0) {
					playerRunsList.set(j, census2);
					playerRunsList.set(j + 1, census1);
				}
			}
		}
	}

	private void sortBowlerData(Comparator<IPLBowling> comparator) {
		for (int i = 0; i < bowlerDataList.size() - 1; i++) {
			for (int j = 0; j < bowlerDataList.size() - i - 1; j++) {
				IPLBowling census1 = bowlerDataList.get(j);
				IPLBowling census2 = bowlerDataList.get(j + 1);
				if (comparator.compare(census1, census2) > 0) {
					bowlerDataList.set(j, census2);
					bowlerDataList.set(j + 1, census1);
				}
			}
		}
	}

}
