/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2020  Vitalii Ananev <an-vitek@ya.ru>
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

import lombok.RequiredArgsConstructor;
import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.Worksheet;
import org.spacious_team.table_wrapper.api.AbstractReportPage;
import org.spacious_team.table_wrapper.api.TableCellAddress;

import java.util.function.BiPredicate;

@RequiredArgsConstructor
public class XmlReportPage extends AbstractReportPage<XmlTableRow> {

    private final Worksheet sheet;

    @Override
    public TableCellAddress find(Object value, int startRow, int endRow, int startColumn, int endColumn,
                                 BiPredicate<String, Object> stringPredicate) {
        return XmlTableHelper.find(sheet, value, startRow, endRow, startColumn, endColumn, stringPredicate);
    }

    @Override
    public XmlTableRow getRow(int i) {
        Row row = sheet.getRowAt(i + 1);
        return (row == null) ? null : new XmlTableRow(row);
    }

    @Override
    public int getLastRowNum() {
        return XmlTableHelper.getLastRowNum(sheet);
    }
}
