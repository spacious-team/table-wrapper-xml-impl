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

import nl.fountain.xelem.excel.Worksheet;
import nl.fountain.xelem.excel.ss.SSWorksheet;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.spacious_team.table_wrapper.api.TableCellAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class XmlReportPageTest {

    @Test
    void findByValue() {
        Worksheet worksheet = getWorksheet();
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(TableCellAddress.of(0, 1),
                reportPage.find("12", 0, 2, 0, 2));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find("12", 1, 2, 0, 2));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find("12", 0, 2, 2, 3));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find("xyz", 0, 2, 0, 2));
    }

    @Test
    void findByPrefix() {
        Worksheet worksheet = getWorksheet();
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(TableCellAddress.of(0, 1),
                reportPage.find(0, 2, 0, 2, "12"::equals));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find(1, 2, 0, 2, "12"::equals));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find(0, 2, 2, 3, "12"::equals));
        assertEquals(TableCellAddress.NOT_FOUND,
                reportPage.find(0, 2, 0, 2, "xyz"::equals));
    }

    @Test
    void getRow() {
        int row = 1;
        Worksheet worksheet = spy(getWorksheet());
        XmlReportPage reportPage = spy(new XmlReportPage(worksheet));

        @Nullable XmlTableRow actual = reportPage.getRow(row);

        assertInstanceOf(XmlTableRow.class, actual);
        verify(worksheet).getRowAt(row + 1);
        assertNull(reportPage.getRow(2));
        assertNull(reportPage.getRow(-1));
    }

    @Test
    void getLastRowNum() {
        Worksheet worksheet = getWorksheet();
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(1, reportPage.getLastRowNum());
    }

    @Test
    void getLastRowNumEmptyPage() {
        Worksheet worksheet = new SSWorksheet("test sheet");
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(-1, reportPage.getLastRowNum());
    }

    @Test
    void findEmptyRowNoEmpty() {
        Worksheet worksheet = getWorksheet();
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(-1, reportPage.findEmptyRow(0));
    }

    @Test
    void findEmptyRowOnEmptySheet() {
        Worksheet worksheet = new SSWorksheet("test sheet");
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(-1, reportPage.findEmptyRow(0));
    }

    @Test
    void findEmptyRowOnSheetOfEmptyRow() {
        Worksheet worksheet = new SSWorksheet("test sheet");
        worksheet.addRowAt(1, null);
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(0, reportPage.findEmptyRow(0));
    }

    @Test
    void findEmptyRow() {
        Worksheet worksheet = getWorksheet();
        worksheet.addCellAt(3, 1).setData("");
        worksheet.addCellAt(3, 2).setData("");
        XmlReportPage reportPage = new XmlReportPage(worksheet);

        assertEquals(2, reportPage.findEmptyRow(0));
    }

    private static Worksheet getWorksheet() {
        Worksheet worksheet = new SSWorksheet("test sheet");
        worksheet.addCellAt(1, 1).setData("11");
        worksheet.addCellAt(1, 2).setData("12");
        worksheet.addCellAt(2, 1).setData("21");
        worksheet.addCellAt(2, 2).setData("22");
        return worksheet;
    }
}