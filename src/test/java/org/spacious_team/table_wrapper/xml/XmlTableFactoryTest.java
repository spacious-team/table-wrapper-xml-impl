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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spacious_team.table_wrapper.api.TableCellRange;
import org.spacious_team.table_wrapper.xml.XmlTableTest.TableHeader;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XmlTableFactoryTest {

    @Spy
    XmlTableFactory factory;
    @Mock
    XmlReportPage reportPage;
    TableCellRange tableRange = TableCellRange.EMPTY_RANGE;

    @Test
    void create() {
        XmlTable table = (XmlTable)
                factory.create(reportPage, "table name", tableRange, TableHeader.class, 1);

        assertSame(table.getReportPage(), reportPage);
        assertSame(tableRange, table.getTableRange());
        assertTrue(table.isEmpty());
        assertTrue(table.getHeaderDescription().isEmpty());
    }
}