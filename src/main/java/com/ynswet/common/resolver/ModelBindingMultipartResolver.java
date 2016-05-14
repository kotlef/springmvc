package com.ynswet.common.resolver;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class ModelBindingMultipartResolver extends CommonsMultipartResolver {
	/**
	 * _contentTypeVar
	 */
	public final static String CONTENTTYPEVAR = "_contentTypeVar";

	/**
	 * _sizeVar
	 */
	public final static String SIZEVAR = "_sizeVar";

	/**
	 * _fileNameVar
	 */
	public final static String FILENAMEVAR = "_fileNameVar";

	/**
	 * This boolean is private in the super class, but we need to track it here
	 * as well
	 */
	private boolean resolveLazily;

	/**
	 * Overridden to capture private variable in super class that controls lazy
	 * initialization Passes value to super class
	 */
	@Override
	public void setResolveLazily(boolean resolveLazily) {
		this.resolveLazily = resolveLazily;
		super.setResolveLazily(resolveLazily);
	}

	/**
	 * Binds the attributes of each Multipart file in the request to new request
	 * attributes so that these meta values can be mapped into variables
	 * 
	 * @param mpRequest
	 * @throws MultipartException
	 */
	@SuppressWarnings("unchecked")
	public void bindMultipartFileAttributesToRequest(
			MultipartParsingResult parsingResult) throws MultipartException {
		Set<String> fileNames = parsingResult.getMultipartFiles().keySet();

		if (fileNames == null)
			return;

		for (String fileName : fileNames)
			bindMultipartFileDetails(fileName,
					parsingResult.getMultipartParameters(),
					(MultipartFile) (parsingResult.getMultipartFiles()
							.getFirst(fileName)));
	}

	/**
	 * Overridden to intercept the original multi part parsing so that the
	 * additional attributes from each Multipart file can be bound to the
	 * request parameters.
	 */
	@Override
	public MultipartHttpServletRequest resolveMultipart(
			final HttpServletRequest request) throws MultipartException {
		Assert.notNull(request, "Request must not be null");
		if (resolveLazily) {
			return new DefaultMultipartHttpServletRequest(request) {

				@Override
				protected void initializeMultipart() {
					MultipartParsingResult parsingResult = parseRequest(request);
					// bind the additional attributes to the parsing result
					bindMultipartFileAttributesToRequest(parsingResult);
					setMultipartFiles(parsingResult.getMultipartFiles());
					setMultipartParameters(parsingResult
							.getMultipartParameters());
				}
			};
		} else {
			MultipartParsingResult parsingResult = parseRequest(request);
			// bind the additional attributes to the parsing result
			bindMultipartFileAttributesToRequest(parsingResult);
			return new DefaultMultipartHttpServletRequest(request,
					parsingResult.getMultipartFiles(),
					parsingResult.getMultipartParameters(),
					parsingResult.getMultipartParameterContentTypes());
		}
	}

	/**
	 * Adds parameters to the parameter map that represent the meta information
	 * from the multipart file The parameter names that these values will be set
	 * to are passed on the request under well known keys The keys can be
	 * configured through spring or the defaults defined in the statics above
	 * will be used if no additional keys are passed. For example, if the goal
	 * was to map a files size to a variable call currentFile.size, the request
	 * may contain a parameter like currentFile_sizeVar=currentFile.size. This
	 * method will get the size of the file, and set the request parameter
	 * 
	 * @param fileKey
	 * @param parameterMap
	 * @param multipartFile
	 */
	@SuppressWarnings({ "unchecked" })
	public void bindMultipartFileDetails(String fileKey, Map parameterMap,
			MultipartFile multipartFile) {
		String varPath;

		// add content type attribute mapping
		varPath = getAttributeMappingPath(fileKey, CONTENTTYPEVAR, parameterMap);
		addAttributeMapping(varPath, multipartFile.getContentType(),
				parameterMap);

		// add size attribute mapping
		varPath = getAttributeMappingPath(fileKey, SIZEVAR, parameterMap);
		addAttributeMapping(varPath, "" + multipartFile.getSize(), parameterMap);

		// add file attribute mapping
		varPath = getAttributeMappingPath(fileKey, FILENAMEVAR, parameterMap);
		addAttributeMapping(varPath, multipartFile.getOriginalFilename(),
				parameterMap);
	}

	/**
	 * Returns the appropriate map key for the attribute passed which is based
	 * on the File key pluss the attribute key
	 * 
	 * @param fileKey
	 * @param attributeKey
	 */
	@SuppressWarnings("unchecked")
	protected String getAttributeMappingPath(String fileKey,
			String attributeKey, Map varBindingMap) {
		Object attributeMappingPath = varBindingMap.get(fileKey + attributeKey);

		if (attributeMappingPath == null)
			return null;

		// if the attribute is being stored as a String array return the first
		// element
		if (attributeMappingPath instanceof String[]) {
			String[] attr = (String[]) attributeMappingPath;
			if (attr.length > 0)
				return attr[0];
		} else if (attributeMappingPath instanceof String)
			return (String) attributeMappingPath;

		return null;
	}

	/**
	 * Adds non null values to the MutablePropertyValue set passed
	 * 
	 * @param path
	 * @param value
	 * @param mpv
	 */
	@SuppressWarnings("unchecked")
	protected void addAttributeMapping(String path, String value,
			Map requestParameters) {
		if (path == null || value == null)
			return;

		requestParameters.put(path, new String[] { value });
	}

}
