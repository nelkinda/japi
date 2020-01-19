# Copyright © 2016 - 2016 Nelkinda Software Craft Pvt Ltd.
#
# This file is part of com.nelkinda.japi.
#
# com.nelkinda.japi is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
#
# com.nelkinda.japi is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License along with com.nelkinda.japi.
# If not, see <http://www.gnu.org/licenses/>.

## Feature file for testing the CSV editor.
#
# @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
# @version 0.0.2
# @since 0.0.2
Feature: CSV Editor

  Background:
    Given I have just started the csvEditor.

  Scenario: As a <User>, I want to <start the csvEditor> in order to <use it>
    Then the table name must be "<Unnamed>",
    And the csvEditor MUST have focus,
    And the table MUST be empty,
    And the window title MUST be "CSV Editor: <Unnamed>"

  Scenario: As a <User>, I want to <quit the editor> in order to <stop using it>
    When I wait for action "quit",
    Then the window is disposed.

  Scenario: As a <User>, I want to <insert a new column> in an empty table
    Then the csvEditor MUST have focus,
    When I wait for action "columnInsertAfter",
    Then the table content MUST be:
      | Unnamed_A |
    When I wait for action "columnInsertAfter",
    Then the table content MUST be:
      | Unnamed_A | Unnamed_B |
    When I wait for action "columnInsertAfter",
    Then the table content MUST be:
      | Unnamed_A | Unnamed_B | Unnamed_C |

  Scenario: As a <User>, I want to <delete a column> in a non-empty table
    Then the csvEditor MUST have focus,
    When I wait for action "columnInsertAfter",
    Then the table content MUST be:
      | Unnamed_A |
    When I wait for action "columnDelete",
    Then the table MUST be empty,
