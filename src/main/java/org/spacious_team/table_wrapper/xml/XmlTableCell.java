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
import nl.fountain.xelem.excel.Cell;
import org.spacious_team.table_wrapper.api.TableCell;
import org.spacious_team.table_wrapper.api.TableColumnDescription;
import org.spacious_team.table_wrapper.api.TableRow;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class XmlTableCell implements TableCell {

    private static final Pattern spacePattern = Pattern.compile("\\s");

    private final Cell cell;

    @Override
    public int getColumnIndex() {
        return cell.getIndex() - 1;
    }

    @Override
    public Object getValue() {
        return cell.getData();
    }

    @Override
    public int getIntValue() {
        return getDoubleValue().intValue();
    }

    @Override
    public long getLongValue() {
        return getDoubleValue().longValue();
    }

    @Override
    public Double getDoubleValue() {
        Object cellValue = getValue();
        if (cellValue instanceof Number) {
            return ((Number) cellValue).doubleValue();
        } else {
            return Double.parseDouble(spacePattern.matcher(cellValue.toString()).replaceAll(""));
        }
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        double number = getDoubleValue();
        return (number == 0) ? BigDecimal.ZERO : BigDecimal.valueOf(number);
    }

    @Override
    public String getStringValue() {
        return cell.getData$();
    }

    @Override
    public Instant getInstantValue() {
        return ((Date) getValue()).toInstant();
    }

    /**
     * @return local date time at system default time zone
     */
    @Override
    public LocalDateTime getLocalDateTimeValue() {
        return getInstantValue()
                .atZone(ZoneOffset.systemDefault())
                .toLocalDateTime();
    }
}
