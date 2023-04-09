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
import nl.fountain.xelem.excel.ss.SSCell;
import nl.fountain.xelem.excel.ss.SSRow;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spacious_team.table_wrapper.api.TableCell;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.time.ZoneOffset.UTC;
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
    void getRow() {
        assertSame(xmlRow, row.getRow());
    }

    @Test
    void getCell_Null() {
        when(xmlRow.getCellAt(1)).thenReturn(null);

        @Nullable TableCell cell = row.getCell(0);

        assertNull(cell);
        verify(xmlRow).getCellAt(1);
    }

    @Test
    void getCell_nonNull() {
        Cell xmlCell = mock(Cell.class);
        when(xmlRow.getCellAt(1)).thenReturn(xmlCell);

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
        Instant instant = LocalDateTime.of(2023, 4, 5, 20, 44, 1)
                .atZone(UTC)
                .toInstant();
        AtomicInteger adder = new AtomicInteger();
        Map<Integer, Cell> cells = Stream.of(null, "test", true, 'A',
                        1, 2L, 3.1f, 3.2, (byte) 4, (short) 5, BigDecimal.valueOf(6.66), BigInteger.valueOf(7),
                        Date.from(instant), ZonedDateTime.ofInstant(instant, UTC), OffsetDateTime.ofInstant(instant, UTC))
                .map(XmlTableRowTest::toCell)
                .collect(Collectors.toMap($ -> adder.incrementAndGet(), Function.identity()));
        when(xmlRow.getCellMap()).thenReturn(new TreeMap<>(cells));
        when(xmlRow.getIndex()).thenReturn(1);

        assertTrue(row.rowContains(null));
        assertTrue(row.rowContains("test"));
        assertTrue(row.rowContains(true));
        assertTrue(row.rowContains("A"));
        assertTrue(row.rowContains(1));
        assertTrue(row.rowContains(1L));
        assertTrue(row.rowContains(2));
        assertTrue(row.rowContains(2L));
        assertTrue(row.rowContains(3.1f));
        assertTrue(row.rowContains(3.1));
        assertTrue(row.rowContains(3.2f));
        assertTrue(row.rowContains(3.2));
        assertTrue(row.rowContains(4));
        assertTrue(row.rowContains(5));
        assertTrue(row.rowContains(6.66));
        assertTrue(row.rowContains(7));
        assertTrue(row.rowContains(instant));
        assertTrue(row.rowContains(Date.from(instant)));
        assertTrue(row.rowContains(ZonedDateTime.ofInstant(instant, ZoneId.of("Europe/Paris"))));
        assertTrue(row.rowContains(OffsetDateTime.ofInstant(instant, ZoneId.of("Europe/Paris"))));

        assertFalse(row.rowContains("test2"));
        assertFalse(row.rowContains(false));
        assertFalse(row.rowContains('B'));
        assertFalse(row.rowContains("B"));
        assertFalse(row.rowContains(8));
        assertFalse(row.rowContains(BigDecimal.valueOf(9.1)));
        assertFalse(row.rowContains(BigInteger.valueOf(10)));
        assertFalse(row.rowContains(Instant.now()));
        assertFalse(row.rowContains(Date.from(Instant.now())));
        assertFalse(row.rowContains(ZonedDateTime.ofInstant(Instant.now(), UTC)));
        assertFalse(row.rowContains(OffsetDateTime.ofInstant(Instant.now(), UTC)));
        assertFalse(row.rowContains(LocalDateTime.now()));
    }

    @Test
    void iterator() {
        Instant instant = LocalDateTime.of(2023, 4, 5, 20, 44, 1)
                .atZone(UTC)
                .toInstant();
        Collection<@Nullable Cell> cells = Stream.of(null, "test",
                        1, 2L, 3.1f, 3.2, (byte) 4, (short) 5, BigDecimal.valueOf(6.66), BigInteger.valueOf(7),
                        true, Date.from(instant), 'A')
                .map(XmlTableRowTest::toCell)
                .collect(Collectors.toList());
        cells.add(null);
        when(xmlRow.getCells()).thenReturn(cells);
        Collection<@Nullable XmlTableCell> expected = cells.stream()
                .filter(Objects::nonNull)
                .map(XmlTableCell::of)
                .collect(Collectors.toList());
        expected.add(null);

        List<TableCell> actual = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(row.iterator(), 0), false)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    static Cell toCell(Object v) {
        Cell cell = new SSCell();
        if (v instanceof Byte) {
            cell.setData((byte) v);  // no method with reference arg
        } else if (v instanceof Short) {
            cell.setData((short) v);  // no method with reference arg
        } else if (v instanceof Character) {
            cell.setData((char) v);  // no method with reference arg
        } else if (v instanceof BigInteger) {
            cell.setData((Number) v);  // select correct impl
        } else {
            cell.setData(v);
        }
        return cell;
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