/*
 * Table Wrapper Xml SpreadsheetML Impl
 * Copyright (C) 2021  Spacious Team <spacious-team@ya.ru>
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

import lombok.ToString;
import nl.fountain.xelem.excel.Cell;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spacious_team.table_wrapper.api.CellDataAccessObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@ToString
public class XmlCellDataAccessObject implements CellDataAccessObject<Cell, XmlTableRow> {

    public static final XmlCellDataAccessObject INSTANCE = new XmlCellDataAccessObject();

    @Override
    public @Nullable Cell getCell(XmlTableRow row, Integer cellIndex) {
        return row.getRow().getCellAt(cellIndex + 1);
    }

    @Override
    public @Nullable Object getValue(Cell cell) {
        if (!cell.hasData()) {
            return null;
        } else if (Objects.equals(cell.getXLDataType(), "DateTime")) {
            // cell.getData() loses millis part from cell, use workaround
            return Date.from(getInstantValue(cell));
        }
        return cell.getData();
    }

    @Override
    public String getStringValue(Cell cell) {
        return cell.getData$();
    }

    @Override
    public Instant getInstantValue(Cell cell) {
        return getLocalDateTimeValue(cell)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    /**
     * Xml cell contains local date time, timezone info was lost.
     * Assume that the timezone was current system default.
     */
    @Override
    public LocalDateTime getLocalDateTimeValue(Cell cell) {
        String localDateTime = getStringValue(cell);
        return LocalDateTime.parse(localDateTime, ISO_LOCAL_DATE_TIME);
    }

    @Override
    public LocalDateTime getLocalDateTimeValue(Cell cell, ZoneId zoneId) {
        return getLocalDateTimeValue(cell)
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }
}
