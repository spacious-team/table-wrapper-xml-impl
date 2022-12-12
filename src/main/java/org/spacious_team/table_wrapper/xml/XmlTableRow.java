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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.fountain.xelem.excel.Cell;
import nl.fountain.xelem.excel.Row;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spacious_team.table_wrapper.api.AbstractReportPageRow;
import org.spacious_team.table_wrapper.api.TableCell;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static lombok.AccessLevel.PACKAGE;
import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;
import static org.spacious_team.table_wrapper.xml.XmlTableHelper.equalsPredicate;

@RequiredArgsConstructor(staticName = "of")
public class XmlTableRow extends AbstractReportPageRow {

    @Getter(PACKAGE)
    private final Row row;

    @Override
    public @Nullable TableCell getCell(int i) {
        Cell cell = row.getCellAt(i + 1);
        return (cell == null) ? null : XmlTableCell.of(cell);
    }

    @Override
    public int getRowNum() {
        return row.getIndex() - 1;
    }

    @Override
    public int getFirstCellNum() {
        try {
            return row.getCellMap().firstKey() - 1;
        } catch (NoSuchElementException ignore) {
            return -1;
        }
    }

    @Override
    public int getLastCellNum() {
        return row.getCellMap().lastKey() - 1;
    }

    @Override
    public boolean rowContains(Object expected) {
        return XmlTableHelper.find(row, row.getIndex() - 1, 0, Integer.MAX_VALUE, equalsPredicate(expected)) != NOT_FOUND;
    }

    @Override
    public Iterator<@Nullable TableCell> iterator() {
        Function<@Nullable Cell, @Nullable TableCell> converter =
                cell -> (cell == null) ? null : XmlTableCell.of(cell);
        return new ReportPageRowIterator<>(row.getCells().iterator(), converter);
    }
}
