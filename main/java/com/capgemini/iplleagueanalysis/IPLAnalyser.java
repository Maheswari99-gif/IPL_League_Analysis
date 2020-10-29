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
	private Comparator<IPLBatting> censusComparator;

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

	public String getTopBattingAvg() throws IPLAnalyserException {
		if (playerRunsList == null || playerRunsList.size() == 0) {
			throw new IPLAnalyserException("No Census Data", IPLAnalyserException.Exception.NO_CENSUS_DATA);
		}
		double max = playerRunsList.stream().filter(s -> !s.average.equals("-")).map(s -> Double.parseDouble(s.average))
				.max(Double::compare).get();
		List<IPLBatting> player = playerRunsList.stream().filter(s -> s.average.equals(Double.toString(max)))
				.collect(Collectors.toList());
		return player.get(0).player;
	}

	public String getTopStrikeRate() throws IPLAnalyserException {
		if (playerRunsList == null || playerRunsList.size() == 0) {
			throw new IPLAnalyserException("No Census Data", IPLAnalyserException.Exception.NO_CENSUS_DATA);
		}
		double maxStrikeRate = playerRunsList.stream().map(s -> s.strikeRate).max(Double::compare).get();
		List<IPLBatting> player = playerRunsList.stream().filter(s -> s.strikeRate == maxStrikeRate)
				.collect(Collectors.toList());
		return player.get(0).player;
	}

	public String getMaximum6sAnd4s() throws IPLAnalyserException {
		if (playerRunsList == null || playerRunsList.size() == 0) {
			throw new IPLAnalyserException("No Census Data", IPLAnalyserException.Exception.NO_CENSUS_DATA);
		}
		int maxSixesAndFours = playerRunsList.stream().map(s -> s.sixes + s.fours).max(Integer::compare).get();
		List<IPLBatting> player = playerRunsList.stream().filter(s -> s.sixes + s.fours == maxSixesAndFours)
				.collect(Collectors.toList());
		return player.get(0).player;
	}

	public String getBestStrickRateMaximum6sAnd4s() throws IPLAnalyserException {
		checkForData();
		censusComparator = Comparator.comparing(s -> s.sixes + s.fours);
		censusComparator = censusComparator.thenComparing(s -> s.strikeRate);
		this.sortBatsmenData(censusComparator);
		Collections.reverse(playerRunsList);
		return playerRunsList.get(0).player;
	}

	public void checkForData() throws IPLAnalyserException {
		if (playerRunsList == null || playerRunsList.size() == 0) {
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
}
