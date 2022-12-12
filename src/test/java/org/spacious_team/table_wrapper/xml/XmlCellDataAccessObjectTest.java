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
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
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
    void getValueNull() {
        when(cell.hasData()).thenReturn(false);
        assertNull(dao.getValue(cell));
    }

    @Test
    void getValue() {
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
        Date expected = spy(new Date());
        //noinspection ConstantConditions
        when(dao.getValue(cell)).thenReturn(expected);

        dao.getInstantValue(cell);

        verify(dao).getValue(cell);
        verify(expected).toInstant();
    }
}