package com.tsunami.run.happyrun.utils.music;

import java.util.Comparator;
/**
 * @author wangshujie
 */
public class PinyinComparator implements Comparator<Mp3Info> {

	public int compare(Mp3Info o1, Mp3Info o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
