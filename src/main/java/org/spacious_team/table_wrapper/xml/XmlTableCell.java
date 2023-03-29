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

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.fountain.xelem.excel.Cell;
import org.spacious_team.table_wrapper.api.AbstractTableCell;

@ToString
@EqualsAndHashCode(callSuper = false)
public class XmlTableCell extends AbstractTableCell<Cell, XmlCellDataAccessObject> {

    public static XmlTableCell of(Cell cell) {
        return of(cell, XmlCellDataAccessObject.INSTANCE);
    }

    public static XmlTableCell of(Cell cell, XmlCellDataAccessObject dao) {
        return new XmlTableCell(cell, dao);
    }

    private XmlTableCell(Cell cell, XmlCellDataAccessObject dao) {
        super(cell, dao);
    }

    @Override
    public int getColumnIndex() {
        return getCell().getIndex() - 1;
    }

    @Override
    protected XmlTableCell createWithCellDataAccessObject(XmlCellDataAccessObject dao) {
        return new XmlTableCell(getCell(), dao);
    }

    @EqualsAndHashCode.Include
    @SuppressWarnings("unused")
    private Object getCellForEquals() {
        return getCell();
    }

    @SuppressWarnings("unused")
    @ToString.Include(name = "value")
    private String getCellData() {
        return getStringValue();
    }
}
