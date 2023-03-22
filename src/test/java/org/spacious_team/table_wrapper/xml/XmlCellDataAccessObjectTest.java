/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlCellDataAccessObjectTest {

    @Spy
    XmlCellDataAccessObject dao;

    @Mock
    XmlTableRow row;

    @Mock
    Cell cell;

    @Test
    void getCell() {
        int index = 2;
        Row xmlRow = mock(Row.class);
        when(row.getRow()).thenReturn(xmlRow);

        dao.getCell(row, index);

        verify(row).getRow();
        verify(xmlRow).getCellAt(index + 1);
    }

    @Test
    void getValue_null() {
        when(cell.hasData()).thenReturn(false);
        assertNull(dao.getValue(cell));
    }

    @Test
    void getValue_date() {
        when(cell.hasData()).thenReturn(true);
        when(cell.getXLDataType()).thenReturn("DateTime");
        doReturn(Instant.now()).when(dao).getInstantValue(cell);

        dao.getValue(cell);

        verify(cell, never()).getData();
        verify(dao).getInstantValue(cell);
    }

    @Test
    void getValue_otherTypes() {
        when(cell.hasData()).thenReturn(true);
        dao.getValue(cell);
        verify(cell).getData();
    }

    @Test
    void getStringValue() {
        dao.getStringValue(cell);
        verify(cell).getData$();
    }

    @Test
    void getInstantValue() {
        String dateTime = "2023-03-22T06:33:00";
        Instant expected = LocalDateTime.of(2023, 3, 22, 6, 33, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        when(dao.getStringValue(cell)).thenReturn(dateTime);

        Instant actual = dao.getInstantValue(cell);

        assertEquals(expected, actual);
        verify(dao).getLocalDateTimeValue(cell);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2023-03-22T06:33:00",
            "2023-03-22T06:33:00.100",
            "2023-03-22T06:33:00.100200",
            "2023-03-22T06:33:00.100200300"})
    void getLocalDateTime(String dateTime) {
        LocalDateTime expected = LocalDateTime.parse(dateTime, ISO_LOCAL_DATE_TIME);
        when(dao.getStringValue(cell)).thenReturn(dateTime);

        LocalDateTime actual = dao.getLocalDateTimeValue(cell);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"UTC", "Europe/Moscow", "Asia/Novosibirsk"})
    void getLocalDateTimeWithZoneId(String zoneIdName) {
        String dateTime = "2023-03-22T06:33:00";
        ZoneId zoneId = ZoneId.of(zoneIdName);
        LocalDateTime expected = LocalDateTime.of(2023, 3, 22, 6, 33, 0)
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
        when(dao.getStringValue(cell)).thenReturn(dateTime);

        LocalDateTime actual = dao.getLocalDateTimeValue(cell, zoneId);

        assertEquals(expected, actual);
        verify(dao).getLocalDateTimeValue(cell);
    }

    @Test
    void testToString() {
        assertEquals("XmlCellDataAccessObject()", dao.toString());
    }
}