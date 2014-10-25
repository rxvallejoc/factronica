/*
 * Licensed by the authors under the Creative Commons
 * Attribution-ShareAlike 2.0 Generic (CC BY-SA 2.0)
 * License:
 * 
 * http://creativecommons.org/licenses/by-sa/2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obiectumclaro.factronica.core.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author <a href="mailto:rxvallejo@obiectumclaro.com">Rodrigo Vallejo</a>
 */
@Stateless
@LocalBean
public class JaxbMarshallerBean {
    
    /**
     * 
     * @param document
     * @return
     * @throws JAXBException
     * @throws IOException
     */
    public <T> byte[] marshall(T document) throws JAXBException, IOException {
        StringWriter xmlStringWriter = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(document.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(document, xmlStringWriter);
        xmlStringWriter.close();
        return xmlStringWriter.toString().getBytes("UTF-8");
    }

    
    /**
     * 
     * @param document
     * @param type
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public <T> T unmarshall(final Class<T> type,byte[] document) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        T object=(T) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(document));
        return  object;
    }

}
