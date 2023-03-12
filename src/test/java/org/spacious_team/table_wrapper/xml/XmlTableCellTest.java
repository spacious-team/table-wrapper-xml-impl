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
import java.util.Date;

import static java.util.Calendar.MARCH;
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
        //noinspection deprecation
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
                new Date(2023, MARCH, 13, 0, 31, 1)
        };
    }

    //TODO other get methods

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