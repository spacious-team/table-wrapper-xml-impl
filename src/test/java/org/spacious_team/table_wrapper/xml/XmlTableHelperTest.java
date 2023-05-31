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

import nl.fountain.xelem.excel.Worksheet;
import nl.fountain.xelem.excel.ss.SSWorksheet;
import org.junit.jupiter.api.Test;
import org.spacious_team.table_wrapper.api.TableCellAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class XmlTableHelperTest {

    @Test
    void find() {
        Worksheet worksheet = new SSWorksheet("sheet1");
        worksheet.addCellAt(i(0), i(0)).setData("00");
        worksheet.addCellAt(i(0), i(1)).setData("01");
        worksheet.addCellAt(i(1), i(0)).setData("11");
        worksheet.addCellAt(i(1), i(1)).setData(12);
        worksheet.addCellAt(i(2), i(0)).setData((Object) null);
        worksheet.addCellAt(i(2), i(1)).setData("22");

        assertEquals(TableCellAddress.of(1, 0),
                XmlTableHelper.find(worksheet, "11", 0, 3, 0, 2));
        assertEquals(TableCellAddress.of(1, 0),
                XmlTableHelper.find(worksheet, "11",
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(TableCellAddress.of(1, 1),
                XmlTableHelper.find(worksheet, 12, 0, 3, 0, 2));
        assertEquals(TableCellAddress.of(2, 0),
                XmlTableHelper.find(worksheet, null, 0, 3, 0, 2));
        assertSame(TableCellAddress.NOT_FOUND,
                XmlTableHelper.find(worksheet, "00", 1, 3, 0, 2));
        assertSame(TableCellAddress.NOT_FOUND,
                XmlTableHelper.find(worksheet, "00", 0, 3, 1, 2));
        assertSame(TableCellAddress.NOT_FOUND,
                XmlTableHelper.find(worksheet, "00", -1, 0, 1, 2));
        assertSame(TableCellAddress.NOT_FOUND,
                XmlTableHelper.find(worksheet, "00", 0, 3, -1, 0));
    }

    static int i(int index) {
        // XML table uses 1-based indices
        return index + 1;
    }
}