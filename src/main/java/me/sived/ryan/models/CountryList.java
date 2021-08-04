package me.sived.ryan.models;

import java.util.ArrayList;
import java.util.List;

public class CountryList {

    final List<String> UKRAINE_LIST = List.of("KBP", "HRK", "ODS", "LWO", "KHE");
    final List<String> SPAIN_LIST = List.of("ALC", "LEI", "BCN", "GRO", "REU", "BIO", "VLC", "CDT", "FUE", "GCA", "IBZ", "XRY", "ACE", "MAD",
            "AGP", "MAH", "RMU", "PMI", "SDR", "SVQ", "TFN", "TFN", "SCQ", "VLC", "VLL", "VGO", "VIT", "ZAZ");
    final List<String> AUSTRIA_LIST = List.of("VIE", "SZG");
    final List<String> BELGIUM_LIST = List.of("CRL", "BRU");
    final List<String> BOSNIA_LIST = List.of("BNX", "TZL");
    final List<String> BULGARIA_LIST = List.of("BOJ", "PDV", "SOF", "VAR");
    final List<String> CROATIA_LIST = List.of("DBV", "PUY", "SPU", "ZAD", "ZAG");
    final List<String> CYPRUS_LIST = List.of("LCA", "PFO");
    final List<String> CZECHIA_LIST = List.of("BRQ", "OSR", "PED", "PRG");
    final List<String> DENMARK_LIST = List.of("AAL", "AAR", "BLL", "CPH");
    final List<String> ESTONIA_LIST = List.of("TLL");
    final List<String> FINLAND_LIST = List.of("HEL", "LPP", "TMP");
    final List<String> FRANCE_LIST = List.of("EGC", "BZR", "BIQ", "BOD", "BES", "BVE", "CCF", "DLE", "FSC", "GNB", "LRH", "LIL", "LIG", "LDE", "MRS",
            "NTE", "NCE", "FNI", "BVA", "XCR", "PGF", "PIS", "RDZ", "TLS", "TUF");
    final List<String> GERMANY_LIST = List.of("BER", "BRE", "CGN", "DTM", "DRS", "NRN", "ERF", "HHN", "FRA", "HAM", "FKB", "FMM", "FMO", "NUE");
    final List<String> GREECE_LIST = List.of("ATH", "CHQ", "CFU", "HER", "KLX", "EFL", "KGS", "JMK", "PVK", "RHO", "JTR", "JSI", "SKG", "ZTH");
    final List<String> HUNGARY_LIST = List.of("BUD");
    final List<String> IRELAND_LIST = List.of("ORK", "DUB", "KIR", "NOC", "SNN");
    final List<String> ISRAEL_LIST = List.of("TLV");
    final List<String> ITALY_LIST = List.of("AHO", "AOI", "BRI", "BLQ", "BDS", "CAG", "CTA", "CIY", "CRV", "CUF", "GOA", "SUF", "BGY", "MXP", "NAP", "PMO", "PMF",
            "PEG", "PSR", "PSA", "RMI", "CIA", "FCO", "TPS", "TRS", "TRN", "VCE", "TSF", "VRN");
    final List<String> JORDAN_LIST = List.of("AMM", "AQJ");
    final List<String> LATVIA_LIST = List.of("RIX");
    final List<String> LITHUANIA_LIST = List.of("VNO", "KUN", "PLQ");
    final List<String> LUXEMBOURG_LIST = List.of("LUX");
    final List<String> MALTA_LIST = List.of("MLA");
    final List<String> MONTENEGRO_LIST = List.of("TGD");
    final List<String> MOROCCO_LIST = List.of("AGA", "ESU", "FEZ", "RAK", "NDR", "OZZ", "OUD", "RBA", "TNG", "TTU");
    final List<String> NETHERLANDS_LIST = List.of("AMS", "EIN", "MST");
    final List<String> NORWAY_LIST = List.of("OSL", "TRP");
    final List<String> POLAND_LIST = List.of("BZG", "GDN", "KTW", "KRK", "LCJ", "LUZ", "SZY", "POZ", "RZE", "SZZ", "WMI", "WRO");
    final List<String> PORTUGAL_LIST = List.of("OPO", "FAO", "LIS", "PDL", "TER");
    final List<String> ROMANIA_LIST = List.of("OTP", "CLJ", "OMR", "SBZ", "SCV", "TSR");
    final List<String> SERBIA_LIST = List.of("NIS");
    final List<String> SLOVAKIA_LIST = List.of("BTS", "KSC");
    final List<String> SWEDEN_LIST = List.of("GOT", "MMX", "ARN", "NYO", "VST", "VXO");
    final List<String> SWITZERLAND_LIST = List.of("BSL", "GVA", "ZRH");
    final List<String> TURKEY_LIST = List.of("BJV", "DLM");
    final List<String> UK_LIST = List.of("ABZ", "BHD", "BFS", "BHX", "BOH", "BRS", "CWL", "EMA", "EDI", "EXT", "GLA", "PIK", "LBA", "LPL", "LGW", "LTN", "SEN",
            "SEN", "STN", "MAN", "NCL", "NQY", "MME");

    public List<String> getFor(String countryCode) {
        return switch (countryCode) {
            case "UA" -> UKRAINE_LIST;
            case "ES" -> SPAIN_LIST;
            case "AT" -> AUSTRIA_LIST;
            case "BE" -> BELGIUM_LIST;
            case "BA" -> BOSNIA_LIST;
            case "BG" -> BULGARIA_LIST;
            case "HR" -> CROATIA_LIST;
            case "CY" -> CYPRUS_LIST;
            case "CZ" -> CZECHIA_LIST;
            case "DK" -> DENMARK_LIST;
            case "EE" -> ESTONIA_LIST;
            case "FI" -> FINLAND_LIST;
            case "FR" -> FRANCE_LIST;
            case "DE" -> GERMANY_LIST;
            case "GR" -> GREECE_LIST;
            case "HU" -> HUNGARY_LIST;
            case "IE" -> IRELAND_LIST;
            case "IL" -> ISRAEL_LIST;
            case "IT" -> ITALY_LIST;
            case "JO" -> JORDAN_LIST;
            case "LV" -> LATVIA_LIST;
            case "LT" -> LITHUANIA_LIST;
            case "LU" -> LUXEMBOURG_LIST;
            case "MT" -> MALTA_LIST;
            case "ME" -> MONTENEGRO_LIST;
            case "MA" -> MOROCCO_LIST;
            case "NL" -> NETHERLANDS_LIST;
            case "NO" -> NORWAY_LIST;
            case "PL" -> POLAND_LIST;
            case "PT" -> PORTUGAL_LIST;
            case "RO" -> ROMANIA_LIST;
            case "RS" -> SERBIA_LIST;
            case "SK" -> SLOVAKIA_LIST;
            case "SE" -> SWEDEN_LIST;
            case "CH" -> SWITZERLAND_LIST;
            case "TR" -> TURKEY_LIST;
            case "UK" -> UK_LIST;
            default -> new ArrayList<>(List.of(countryCode));
        };

    }
}
