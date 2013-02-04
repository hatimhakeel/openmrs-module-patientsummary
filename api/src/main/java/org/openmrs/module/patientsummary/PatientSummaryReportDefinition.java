/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientsummary;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.column.definition.ColumnDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

/**
 * A {@link ReportDefinition} subclass that represents the metadata that describes a particular
 * patient summary report that can be evaluated.
 * 
 * @see ReportDefinition
 */
@Localized("patientsummary.PatientSummaryReportDefinition")
public class PatientSummaryReportDefinition extends ReportDefinition {
	
	public static final String DEFAULT_DATASET_KEY = "patient";
	
	/**
	 * Default Constructor
	 */
	public PatientSummaryReportDefinition() {
		setPatientDataSetDefinition(new PatientDataSetDefinition());
	}
	
	/**
	 * Overrides the default behavior, such that only a single PatientDataSetDefinition is supported
	 */
	@Override
	public void addDataSetDefinition(String key, Mapped<? extends DataSetDefinition> definition) {
		throw new PatientSummaryException("The PatientSummaryReportDefinition does not support multiple DataSetDefinitions");
	}
	
	/**
	 * @return the underlying PatientDataSetDefinition
	 */
	public PatientDataSetDefinition getPatientDataSetDefinition() {
		return (PatientDataSetDefinition) getDataSetDefinitions().get(DEFAULT_DATASET_KEY).getParameterizable();
		
	}
	
	/**
	 * @return the underlying PatientDataSetDefinition
	 */
	public void setPatientDataSetDefinition(PatientDataSetDefinition pdsd) {
		getDataSetDefinitions().put(DEFAULT_DATASET_KEY, new Mapped<DataSetDefinition>(pdsd, null));
	}
	
	/**
	 * @return the simplified data schema
	 */
	public Map<String, String> getDataSchema() {
		Map<String, String> dataSchema = new LinkedHashMap<String, String>();
		
		for (ColumnDefinition column : getPatientDataSetDefinition().getColumnDefinitions()) {
			for (DataSetColumn dataSetColumn : column.getDataSetColumns()) {
				Class<?> dataType = dataSetColumn.getDataType();
				dataSchema.put(dataSetColumn.getName(), splitCamelCase(dataType.getSimpleName()));
			}
		}
		
		return dataSchema;
	}
	
	/**
	 * Copied from http://stackoverflow.com/a/2560017
	 * <p>
	 * By http://stackoverflow.com/users/276101/polygenelubricants
	 */
	private String splitCamelCase(String s) {
		return s.replaceAll(
		    String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
}
