/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2020 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.isf.lab.dto;

public class LaboratoryForPrintDTO {

    private String exam;

    private String date;

    private String result;

    private Integer code;

	public String getExam() {
		return this.exam;
	}

	public String getDate() {
		return this.date;
	}

	public String getResult() {
		return this.result;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
