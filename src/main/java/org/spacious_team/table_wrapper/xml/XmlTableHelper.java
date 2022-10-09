/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
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
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;

class XmlTableHelper {

    static TableCellAddress find(Worksheet sheet, Object expected,
                                 int startRow, int endRow,
                                 int startColumn, int endColumn) {
        return find(sheet, startRow, endRow, startColumn, endColumn, equalsPredicate(expected));
    }

    static TableCellAddress find(Worksheet sheet, int startRow, int endRow,
                                 int startColumn, int endColumn,
                                 Predicate<Cell> predicate) {
        startRow = Math.max(0, startRow);
        endRow = Math.min(endRow, getLastRowNum(sheet) + 1); // endRow is exclusive
        for (int rowNum = startRow; rowNum < endRow; rowNum++) {
            Row row = sheet.getRowAt(rowNum + 1);
            TableCellAddress address = find(row, startColumn, endColumn, predicate);
            if (address != NOT_FOUND) {
                return address;
            }
        }
        return NOT_FOUND;
    }

    static TableCellAddress find(Row row, int startColumn, int endColumn, Predicate<Cell> predicate) {
        if (row != null) {
            for (Cell cell : row.getCells()) {
                if (cell != null) {
                    int column = cell.getIndex() - 1;
                    if (startColumn <= column && column < endColumn) {
                        if (predicate.test(cell)) {
                            return new TableCellAddress(row.getIndex() - 1, column);
                        }
                    }
                }
            }
        }
        return NOT_FOUND;
    }

    static int getLastRowNum(Worksheet sheet) {
        return sheet.getTable().getRowMap().lastKey() - 1;
    }

    static Predicate<Cell> equalsPredicate(Object expected) {
        if (expected instanceof String) {
            return (cell) -> Objects.equals(cell.getData$(), expected);
        } else if (expected instanceof Integer || expected instanceof Long) {
            return (cell) -> {
                Object data = cell.getData();
                return (data instanceof Number) &&
                        ((Number) data).longValue() == ((Number) expected).longValue();
            };
        } else if (expected instanceof Number) {
            return (cell) -> {
                Object data = cell.getData();
                return (data instanceof Number) &&
                        Math.abs(((Number) data).doubleValue() - ((Number) expected).doubleValue()) < 1e-6;
            };
        } else if (expected instanceof Boolean) {
            return (cell) -> Objects.equals(expected, cell.booleanValue());
        } else {
            return (cell) -> Objects.equals(expected, cell.getData());
        }
    }
}
