package eu.karelhovorka.zpevnik.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Tone {
	// @formatter:off
	C(new CountrySpecificToneName("C", CountryCategory.EASTERN, 0, "His", "H#"), new CountrySpecificToneName("C", CountryCategory.WESTERN, 0, "Bis", "B#")),
	CIS(new CountrySpecificToneName("C#", "Db", "Cis", "Des", CountryCategory.EASTERN, 1), new CountrySpecificToneName("C#", "Db", "Cis", "Des", CountryCategory.WESTERN, 1)),
	D(new CountrySpecificToneName("D", CountryCategory.EASTERN, 2), new CountrySpecificToneName("D", CountryCategory.WESTERN, 2)), 
	DIS(new CountrySpecificToneName("D#", "Eb", "Dis", "Es", CountryCategory.EASTERN, 3), new CountrySpecificToneName("D#", "Eb", "Dis", "Es", CountryCategory.WESTERN, 3)),
	//E(new AreaTone("E", "Fb", "E", "Fes", ToneNameType.EASTERN, 4), new AreaTone("E", "Fb", "E", "Fes", ToneNameType.WESTERN, 4)),
	E(new CountrySpecificToneName("E", CountryCategory.EASTERN, 4, "Fes", "Fb"), new CountrySpecificToneName("E", CountryCategory.WESTERN, 4, "Fes", "Fb")),
	//F(new AreaTone("E#", "F", "Eis", "F", ToneNameType.EASTERN, 5), new AreaTone("E#", "F", "Eis", "F", ToneNameType.WESTERN, 5)),
	F(new CountrySpecificToneName("F", CountryCategory.EASTERN, 5, "Eis", "E#"), new CountrySpecificToneName("F", CountryCategory.WESTERN, 5, "Eis", "E#")),
	FIS(new CountrySpecificToneName("F#", "Gb", "Fis", "Ges", CountryCategory.EASTERN, 6), new CountrySpecificToneName("F#", "Gb", "Fis", "Ges", CountryCategory.WESTERN, 6)),
	G(new CountrySpecificToneName("G", CountryCategory.EASTERN, 7), new CountrySpecificToneName("G", CountryCategory.WESTERN, 7)),
	GIS(new CountrySpecificToneName("G#", "Ab", "Gis", "As", CountryCategory.EASTERN, 8), new CountrySpecificToneName("G#", "Ab", "Gis", "As", CountryCategory.WESTERN, 8)),
	A(new CountrySpecificToneName("A", CountryCategory.EASTERN, 9), new CountrySpecificToneName("A", CountryCategory.WESTERN, 9)),
	AIS(new CountrySpecificToneName("A#", "B", "Ais", "B", CountryCategory.EASTERN, 10, "Hb", "Hes"), new CountrySpecificToneName("A#", "Bb", "Ais", "Bb", CountryCategory.WESTERN, 10)),
	H(new CountrySpecificToneName("H", CountryCategory.EASTERN, 11, "Cb", "H", "Ces"), new CountrySpecificToneName("B", CountryCategory.WESTERN, 11, "H", "Cb", "B", "Ces")),
	
	;
	
	// @formatter:on
	/*
	 * C( 0, "C", "His", "H#"), Cis(1, "C#", "Db", "Cis", "Des"), D(2, "D"),
	 * Dis(3, "D#", "Eb", "Dis", "Es"), E(4, "E", "Fes", "Fb"), F(5, "F", "Eis",
	 * "E#"), Fis(6, "F#", "Gb", "Fis", "Ges"), G(7, "G"), Gis( 8, "G#", "Ab",
	 * "Gis", "As"), A(9, "A"), Ais(10, "A#", "Hb", "Ais", "B"), H(11, "H",
	 * "Cb", "Ces");
	 */
	private Map<CountryCategory, CountrySpecificToneName> aliases = new HashMap<CountryCategory, CountrySpecificToneName>();

	private int index;

	public enum CountryCategory {
		WESTERN, EASTERN;
	}

	public enum ModificationAbbreviation {
		SHARP, FLAT, IS, ES;
	}

	static class CountrySpecificToneName {
		private CountryCategory type;

		private List<String> aliases = new ArrayList<String>();

		private int index;

		private Map<ModificationAbbreviation, String> names = new HashMap<ModificationAbbreviation, String>();

		public CountrySpecificToneName(String name, CountryCategory type, int index, String... extraAliases) {
			this(name, name, name, name, type, index, extraAliases);
		}

		public CountrySpecificToneName(String sharp, String flat, String is, String es, CountryCategory type, int index, String... extraAliases) {
			names.put(ModificationAbbreviation.SHARP, sharp);
			names.put(ModificationAbbreviation.FLAT, flat);
			names.put(ModificationAbbreviation.IS, is);
			names.put(ModificationAbbreviation.ES, es);
			names = Collections.unmodifiableMap(names);
			addAlias(sharp);
			addAlias(flat);
			addAlias(is);
			addAlias(es);
			this.index = index;
			this.type = type;
			for (String alias : extraAliases) {
				addAlias(alias);
			}
			this.aliases = Collections.unmodifiableList(this.aliases);
		}

		private void addAlias(String alias) {
			aliases.add(alias);
		}

		public String getValue(ModificationAbbreviation type) {
			return names.get(type);
		}

		public List<String> getAliases() {
			return aliases;
		}

	}

	private Tone(CountrySpecificToneName... areaTones) {
		this.index = areaTones[0].index;
		for (CountrySpecificToneName at : areaTones) {
			this.aliases.put(at.type, at);
		}
	}

	public Tone transpose(int step) {
		Tone c = this;
		int index = (c.index + step + values().length) % values().length;
		return values()[index];
	}

	public static Tone fromIndex(int index) {
        int normalizedIndex = (index % values().length + values().length) % values().length;
        for (Tone t: values()) {
            if (t.index == normalizedIndex) {
                return t;
            }
        }
        throw new IllegalArgumentException("index: " + index);
    }

	public CountrySpecificToneName getAreaTone(CountryCategory type) {
		return aliases.get(type);
	}

	public String getValue(CountryCategory type, ModificationAbbreviation halfToneType) {
		return this.aliases.get(type).getValue(halfToneType);
	}

}
