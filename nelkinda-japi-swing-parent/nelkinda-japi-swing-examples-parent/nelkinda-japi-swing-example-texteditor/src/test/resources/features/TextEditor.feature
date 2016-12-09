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

Feature: TextEditor
  Background:
    Given I have just started the textEditor.

  Scenario: As a <User>, I want to <start the textEditor> in order to <use it>
    Then the document name must be "<Unnamed>",
    And the textEditor has focus,
    And the document must have the following content:
      """
      """
    And the window title must be "TextEditor: <Unnamed>".

  Scenario: As a <User>, I want to <enter text> in order to <create and edit content which is textual data>
    When I enter the text "foo",
    Then the document must have the following content:
      """
      foo
      """

  Scenario: As a <User>, I want to <create a new file> in order to <create new content from scratch>
    Given I enter the text "foo",
    When I wait for action "new",
    Then the document must have the following content:
      """
      """
    And the document name must be "<Unnamed>",
    And the window title must be "TextEditor: <Unnamed>".

  Scenario: As a <User>, I want to <save a file> in order to <persist my work>
    Given the file "foo.txt" does not exist,
    And I enter the text "This is some text.",
    When I action "save",
    Then I must be asked for a filename.
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    Then the file "foo.txt" must have the following content:
      """
      This is some text.
      """
    And the document name must be "foo.txt",
    And the window title must be "TextEditor: foo.txt".
    When I enter the text " This is some more text.",
    When I action "save",
    Then I must not be asked for a filename,
    When I wait for I/O to be completed,
    Then the file "foo.txt" must have the following content:
      """
      This is some text. This is some more text.
      """
    When I wait for action "new",
    Then the document must have the following content:
      """
      """
    And the document name must be "<Unnamed>",
    And the window title must be "TextEditor: <Unnamed>".
    When I action "save",
    Then I must be asked for a filename.
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    Then the file "foo.txt" must have the following content:
      """
      """
    And the document name must be "foo.txt",
    And the window title must be "TextEditor: foo.txt".

  Scenario: As a <User>, I want to <load a file> in order to <continue my work>
    Given the file "foo.txt" has the following content:
      """
      Hello, World!
      """
    When I action "open",
    Then I must be asked for a filename,
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    Then the document must have the following content:
      """
      Hello, World!
      """
    And the document name must be "foo.txt".

  Scenario: As a <User>, I want to <save a file under a new name> in order to <create derived work>
    Given the file "bar.txt" does not exist,
    And the file "foo.txt" has the following content:
      """
      Hello, World!
      """
    When I action "open",
    Then I must be asked for a filename,
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    And I action "saveAs",
    Then I must be asked for a filename,
    When I enter the filename "bar.txt",
    And I wait for I/O to be completed,
    Then the file "bar.txt" must have the following content:
      """
      Hello, World!
      """

  Scenario: As a <User>, I want to <copy text via the clipboard> in order to <reuse text>
    Given the system clipboard is empty,
    And I enter the text "It's a fine day today.",
    When I mark from position 7 to position 12,
    And I wait for action "copy-to-clipboard",
    Then the system clipboard must contain the text "fine ".

  Scenario: As a <User>, I want to <cut text via the system clipboard> in order to <move text>
    Given the system clipboard is empty,
    And I enter the text "It's a fine day today.",
    When I mark from position 7 to position 12,
    And I wait for action "cut-to-clipboard",
    Then the document must have the following content:
      """
      It's a day today.
      """
    Then the system clipboard must contain the text "fine ".

  Scenario: As a <User>, I want to <paste text via the system clipboard> in order to <reuse text>
    Given the system clipboard contains "fine ",
    And I enter the text "It's a day today.",
    When I set the caret to position 7,
    And I wait for action "paste-from-clipboard",
    Then the document must have the following content:
      """
      It's a fine day today.
      """

  Scenario: As a <User>, I want to <undo my last edit action> in order to <correct an accidental change>
    Given I enter the text "It's a day today.",
    When I set the caret to position 7,
    And I enter the text "fine ",
    And I wait for action "undo",
    Then the document must have the following content:
      """
      It's a day today.
      """

  Scenario: As a <User>, I want to <redo my last undo action> in order to <correct an accidental undo>
    Given I enter the text "It's a day today.",
    When I set the caret to position 7,
    And I enter the text "fine ",
    And I wait for action "undo",
    And I wait for action "redo",
    Then the document must have the following content:
      """
      It's a fine day today.
      """

    Scenario: As a <User>, I want to <change the look and feel> in order to <satisfy my taste>
      Given the current look and feel is the cross-platform look and feel,
      When I set the look and feel to "Nimbus",
      Then the look and feel must be "Nimbus".
