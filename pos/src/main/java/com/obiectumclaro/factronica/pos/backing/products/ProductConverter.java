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
package com.obiectumclaro.factronica.pos.backing.products;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;

/**
 * @author <a href="mailto:rxvallejo@obiectumclaro.com">Rodrigo Vallejo</a>
 */
@Named
public class ProductConverter implements Converter{
    
    @EJB
    private ProductEaoBean productEaoBean;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String submittedValue) {
         if (submittedValue.trim().equals("")) {  
                return null;  
            } else { 
                return productEaoBean.findByCode(submittedValue);
            }
    }

    
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Product) value).getCode());  
        }
    }
}
