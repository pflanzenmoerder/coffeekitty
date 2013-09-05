package de.caffeine.kitty.web.caffeine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import de.caffeine.kitty.service.CaffeineDTO;

@SuppressWarnings("serial")
public class GeneratingCSVFileModel extends AbstractReadOnlyModel<File> {
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneratingCSVFileModel.class);

	private final IModel<List<CaffeineDTO>> dataModel;
	private final CaffeineLevelDisplayMode mode;

	public GeneratingCSVFileModel(IModel<List<CaffeineDTO>> dataModel, CaffeineLevelDisplayMode mode) {
		this.dataModel = dataModel;
		this.mode = mode;
	}

	@Override
	public File getObject() {

		ICsvBeanWriter beanWriter = null;
		try {
			final File file = File.createTempFile("caffeine_statistics" + mode.name().toLowerCase() + "_", ".csv");
			beanWriter = new AlwaysQuotingCsvBeanWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE);

			final String[] header = new String[] {"time", "level"};

			final CellProcessor[] processors = new CellProcessor[] {
				new FmtDate("yyyy-MM-dd HH:mm:ss"), 
				new FmtNumber("#.0##############"), 
			};

			// write the header
			beanWriter.writeHeader(header);
			
			// write the customer beans
			for(final CaffeineDTO caffeineLevelDTO : dataModel.getObject() ) {
				beanWriter.write(caffeineLevelDTO, header, processors);
			}

			return file;
			
		} catch (IOException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("caught exception during csv generation", e);
			}
		
		} finally {
			if( beanWriter != null ) {
				try {
					beanWriter.close();
				} catch (IOException e) {
					// ignore, we can't do anything meaningfull here
				}
			}
		}

		return null;
	}

}
