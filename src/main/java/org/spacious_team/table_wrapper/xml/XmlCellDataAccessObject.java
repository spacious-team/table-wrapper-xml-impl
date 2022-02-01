/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2021  Vitalii Ananev <spacious-team@ya.ru>
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
import org.spacious_team.table_wrapper.api.CellDataAccessObject;

import java.time.Instant;
import java.util.Date;

public class XmlCellDataAccessObject implements CellDataAccessObject<Cell, XmlTableRow> {

    public static final XmlCellDataAccessObject INSTANCE = new XmlCellDataAccessObject();

    @Override
    public Cell getCell(XmlTableRow row, Integer cellIndex) {
        return row.getRow().getCellAt(cellIndex + 1);
    }

    @Override
    public Object getValue(Cell cell) {
        return cell.hasData() ? cell.getData() : null;
    }

    @Override
    public String getStringValue(Cell cell) {
        return cell.getData$();
    }

    @Override
    public Instant getInstantValue(Cell cell) {
        return ((Date) getValue(cell)).toInstant();
    }
}
