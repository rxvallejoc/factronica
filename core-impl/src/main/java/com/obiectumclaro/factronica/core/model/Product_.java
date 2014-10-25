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
package com.obiectumclaro.factronica.core.model;

import java.math.BigDecimal;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.obiectumclaro.factronica.core.enumeration.ProductType;

/**
 * @author <a href="mailto:rxvallejo@obiectumclaro.com">Rodrigo Vallejo</a>
 */
@StaticMetamodel(Product.class)
public class Product_ {
    public static volatile SingularAttribute<Product, Long> pk;
    public static volatile SingularAttribute<Product, String> code;
    public static volatile SingularAttribute<Product, String> alternateCode;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile SingularAttribute<Product, String> description;
    public static volatile SingularAttribute<Product, ProductType> productType;
    public static volatile SingularAttribute<Product, BigDecimal> unitPrice;
    public static volatile ListAttribute<Product,TaxValue > taxValueList;
    public static volatile ListAttribute<Product,AdditionalProductInformation > additionalProductInformationList;
}
