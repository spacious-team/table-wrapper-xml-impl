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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spacious_team.table_wrapper.api.AbstractReportPage;
import org.spacious_team.table_wrapper.api.AbstractTable;
import org.spacious_team.table_wrapper.api.TableCellRange;
import org.spacious_team.table_wrapper.api.TableColumn;
import org.spacious_team.table_wrapper.api.TableHeaderColumn;

import java.time.ZoneOffset;

import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XmlTableTest {

    XmlTable xmlTable;
    TableCellRange tableRange = TableCellRange.of(10, 20, 0, 1);

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        AbstractReportPage<XmlTableRow> reportPage = mock(AbstractReportPage.class);
        //noinspection ConstantConditions
        when(reportPage.getRow(tableRange.getFirstRow() + 1)).thenReturn(mock(XmlTableRow.class)); // header row
        xmlTable = new XmlTable(reportPage, "table name", tableRange, TableHeader.class, 1);
    }

    @Test
    void setCellDataAccessObject() {
        XmlCellDataAccessObject dao = mock(XmlCellDataAccessObject.class);
        assertSame(XmlCellDataAccessObject.INSTANCE, xmlTable.getCellDataAccessObject());

        xmlTable.setCellDataAccessObject(dao);

        assertSame(dao, xmlTable.getCellDataAccessObject());
    }

    @Test
    void subTable() {
        int addToTop = 3;
        int addToBottom = -2;
        AbstractTable<?, ?> subTable = (AbstractTable<?, ?>) xmlTable.subTable(addToTop, addToBottom);

        assertSame(xmlTable.getReportPage(), subTable.getReportPage());
        assertEquals(tableRange.addRowsToTop(addToTop).addRowsToBottom(addToBottom),
                subTable.getTableRange());
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(XmlTable.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        XmlCellDataAccessObject dataAccessObject = XmlCellDataAccessObject.of(ZoneOffset.UTC);
        xmlTable.setCellDataAccessObject(dataAccessObject);
        assertEquals("XmlTable(super=AbstractTable(tableName=table name), " +
                        "cellDataAccessObject=XmlCellDataAccessObject(defaultZoneId=Z))",
                xmlTable.toString());
    }


    enum TableHeader implements TableHeaderColumn {
        ;

        @Override
        public TableColumn getColumn() {
            throw new UnsupportedOperationException();
        }
    }
}