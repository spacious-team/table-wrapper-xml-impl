/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2023  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spacious_team.table_wrapper.xml;

import nl.fountain.xelem.excel.ss.SSCell;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.spacious_team.table_wrapper.api.TableCell;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlTableCellTest {

    SSCell ssCell = new SSCell();

    @BeforeEach
    void setUp() {
        ssCell = new SSCell();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 2, 3})
    void getColumnIndex(int colNum) {
        ssCell.setIndex(colNum + 1);
        TableCell cell = XmlTableCell.of(ssCell);

        assertEquals(colNum, cell.getColumnIndex());
    }

    @ParameterizedTest
    @MethodSource("cellValues")
    void getValue(Object expected) {
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        if (expected instanceof Number) {
            expected = ((Number) expected).doubleValue();
        }

        assertEquals(expected, cell.getValue());
    }

    static Object[] cellValues() {
        return new Object[]{
                1,
                2f,
                3d,
                4L,
                false,
                Boolean.TRUE,
                Integer.MAX_VALUE,
                Long.MIN_VALUE,
                BigDecimal.TEN,
                "string data",
                new Date()
        };
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 2014})
    void getIntValue(int expected) {
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getIntValue());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, 2014})
    void getLongValue(long expected) {
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getLongValue());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 10.24, 10.24000, 10.2400000000001, 10.2400000000000000000000000000000000001})
    void getDoubleValue(double expected) {
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getDoubleValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "10.24", "10.24000", "10.2400000000001,", "10.2400000000000000000000000000000000001"})
    void getBigDecimalValue(String value) {
        BigDecimal expected = new BigDecimal(value);
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getBigDecimalValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10.24", "abc", "This is", "Это есть", "true", "0"})
    void getStringValue(String expected) {
        ssCell.setData(expected);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getStringValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-03-13T20:15:30Z", "2023-03-13T20:15:30.123Z"})
    void getInstantValue(String dateTime) {
        Instant expected = Instant.parse(dateTime);
        Date cellValue = Date.from(expected);
        ssCell.setData(cellValue);
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals(expected, cell.getInstantValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-03-13T20:15:30.123456Z", "2023-03-13T20:15:30.123456789Z"})
    void getInstantValue_nanosLost(String dateTime) {
        Instant instant = Instant.parse(dateTime);
        Date cellValue = Date.from(instant);
        ssCell.setData(cellValue);
        TableCell cell = XmlTableCell.of(ssCell);
        // xml cell value lost nanos part, calc expected instant
        Instant expected = Instant.parse("2023-03-13T20:15:30.123Z");

        assertEquals(expected, cell.getInstantValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-03-13T20:15:30Z", "2023-03-13T20:15:30.123Z"})
    void getLocalDateTimeValue(String dateTime) {
        Instant instant = Instant.parse(dateTime);
        Date cellValue = Date.from(instant);
        ssCell.setData(cellValue);
        TableCell cell = XmlTableCell.of(ssCell);
        LocalDateTime expected = instant.atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
        assertEquals(expected, cell.getLocalDateTimeValue());
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(XmlTableCell.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    @Test
    void testToString() {
        SSCell ssCell = new SSCell();
        ssCell.setData("data");
        TableCell cell = XmlTableCell.of(ssCell);
        assertEquals("XmlTableCell(value=data)", cell.toString());
    }
}