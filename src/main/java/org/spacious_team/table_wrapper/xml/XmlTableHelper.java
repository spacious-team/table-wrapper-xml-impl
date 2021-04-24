/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2020  Vitalii Ananev <spacious-team@ya.ru>
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
import nl.fountain.xelem.excel.Worksheet;
import org.spacious_team.table_wrapper.api.TableCellAddress;

import java.util.Date;
import java.util.function.BiPredicate;

import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;

class XmlTableHelper {

    static TableCellAddress find(Worksheet sheet, Object value, int startRow, int endRow, int startColumn, int endColumn,
                                 BiPredicate<String, Object> stringPredicate) {
        int tableLastRowNum = getLastRowNum(sheet);
        if (tableLastRowNum == -1) {
            return NOT_FOUND;
        } else if (endRow > tableLastRowNum) {
            endRow = tableLastRowNum + 1; // endRow is exclusive
        }
        if (value instanceof Number) {
            value = ((Number) value).doubleValue();
        }
        for(int rowNum = startRow; rowNum < endRow; rowNum++) {
            Row row = sheet.getRowAt(rowNum + 1);
            TableCellAddress address;
            if ((address = find(row, value, startColumn, endColumn, stringPredicate)) != null) {
                return address;
            }
        }
        return NOT_FOUND;
    }

    static TableCellAddress find(Row row, Object value, int startColumn, int endColumn,
                                 BiPredicate<String, Object> stringPredicate) {
        if (row != null) {
            for (Cell cell : row.getCells()) {
                if (cell != null) {
                    int column = cell.getIndex() - 1;
                    if (startColumn <= column && column < endColumn) {
                        if (compare(value, cell, stringPredicate)) {
                            return new TableCellAddress(row.getIndex() - 1, column);
                        }
                    }
                }
            }
        }
        return null;
    }

    static int getLastRowNum(Worksheet sheet) {
        return sheet.getTable().getRowMap().lastKey() - 1;
    }

    private static boolean compare(Object value, Cell cell, BiPredicate<String, Object> stringPredicate) {
        if (value instanceof String) {
            return stringPredicate.test(cell.getData$(), value);
        } else if (value instanceof Integer || value instanceof Long) {
            return cell.intValue() == ((Number) value).longValue();
        } else if (value instanceof Number) {
            return Math.abs(cell.doubleValue() - ((Number) value).doubleValue()) < 1e-6;
        } else if (value instanceof Boolean) {
            return value.equals(cell.booleanValue());
        } else if (value instanceof Date) {
            return value.equals(cell.getData());
        } else {
            return false;
        }
    }
}
