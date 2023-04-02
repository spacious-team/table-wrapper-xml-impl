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

import nl.fountain.xelem.excel.Cell;
import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.ss.SSRow;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spacious_team.table_wrapper.api.TableCell;

import java.util.Map;
import java.util.TreeMap;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlTableRowTest {

    @Mock
    Row xmlRow;
    XmlTableRow row;

    @BeforeEach
    void beforeEach() {
        row = XmlTableRow.of(xmlRow);
    }

    @Test
    void getCell_Null() {
        when(xmlRow.getCellAt(eq(1))).thenReturn(null);

        @Nullable TableCell cell = row.getCell(0);

        assertNull(cell);
        verify(xmlRow).getCellAt(1);
    }

    @Test
    void getCell_nonNull() {
        Cell xmlCell = mock(Cell.class);
        when(xmlRow.getCellAt(eq(1))).thenReturn(xmlCell);

        @Nullable TableCell cell = row.getCell(0);

        assertEquals(XmlTableCell.of(xmlCell), cell);
        verify(xmlRow).getCellAt(1);
    }

    @Test
    void getRowNum() {
        when(xmlRow.getIndex()).thenReturn(1);

        int rowNum = row.getRowNum();

        assertEquals(0, rowNum);
        verify(xmlRow).getIndex();
    }

    @Test
    void getFirstCellNum_noCells() {
        assertEquals(-1, row.getFirstCellNum());
    }

    @Test
    void getFirstCellNum_hasCells() {
        Map<Integer, Cell> cells = Map.of(10, mock(Cell.class), 5, mock(Cell.class));
        when(xmlRow.getCellMap()).thenReturn(new TreeMap<>(cells));

        int firstCellNum = row.getFirstCellNum();

        assertEquals(4, firstCellNum);
        verify(xmlRow).getCellMap();
    }

    @Test
    void getLastCellNum_noCells() {
        assertEquals(-1, row.getLastCellNum());
    }

    @Test
    void getLastCellNum_hasCells() {
        Map<Integer, Cell> cells = Map.of(10, mock(Cell.class), 5, mock(Cell.class));
        when(xmlRow.getCellMap()).thenReturn(new TreeMap<>(cells));

        int lastCellNum = row.getLastCellNum();

        assertEquals(9, lastCellNum);
        verify(xmlRow).getCellMap();
    }

    @Test
    void rowContains() {
    }

    @Test
    void iterator() {
    }

    @Test
    void of() {
    }

    @Test
    void getRow() {
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(XmlTableRow.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertTrue(XmlTableRow.of(new SSRow())
                .toString()
                .startsWith("XmlTableRow(row=nl.fountain.xelem.excel.ss.SSRow@"));
    }
}